package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UsersMapper;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import com.example.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户服务实现类
 * <p>
 * 该类实现了用户相关的所有业务逻辑，包括用户注册、登录、信息查询、更新和删除等功能。
 * 使用了Spring缓存机制对用户信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【users(用户表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:41
 */
@Service
@CacheConfig(cacheNames = "users") // 指定该服务类的缓存名称为"users"
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; // 密码加密工具

    /**
     * 用户注册
     * <p>
     * 该方法执行用户注册流程：
     * 1. 检查用户名是否已存在
     * 2. 如果不存在，创建新用户并加密密码
     * 3. 将用户信息保存到数据库
     *
     * @param userRegisterDTO 用户注册数据传输对象，包含用户名和密码
     * @return 注册成功返回true，失败返回false
     * @throws  BusinessException 用户名已存在时抛出异常
     */
    @Override
    @Transactional
    public boolean register(UserRegisterDTO userRegisterDTO) {
        // 检查用户是否已存在
        if (checkUsernameExists(userRegisterDTO.getUsername())) {
            throw new  BusinessException("用户名已存在");
        }

        // 创建新用户
        Users newUser = new Users();
        newUser.setUsername(userRegisterDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword())); // 密码使用BCrypt加密存储
        newUser.setStatus(1); // 默认启用状态
        newUser.setRole(1); // 默认普通用户角色

        // 设置其他可选字段
        if (StringUtils.hasText(userRegisterDTO.getNickname())) {
            newUser.setNickname(userRegisterDTO.getNickname());
        } else {
            newUser.setNickname(userRegisterDTO.getUsername()); // 默认昵称与用户名相同
        }

        if (StringUtils.hasText(userRegisterDTO.getPhone())) {
            newUser.setPhone(userRegisterDTO.getPhone());
        }

        if (StringUtils.hasText(userRegisterDTO.getEmail())) {
            newUser.setEmail(userRegisterDTO.getEmail());
        }

        return usersMapper.insert(newUser) > 0;
    }

    /**
     * 用户登录
     * <p>
     * 该方法执行用户登录验证：
     * 1. 根据用户名查询用户
     * 2. 验证密码是否匹配
     * 3. 登录成功后，用户信息将被缓存
     *
     * @param userLoginDTO 用户登录数据传输对象，包含用户名和密码
     * @return 登录成功返回用户对象
     * @throws RuntimeException 用户名不存在或密码错误时抛出异常
     */
    @Override
    @Cacheable(value = "users", key = "#userLoginDTO.username") // 缓存登录用户信息，减少数据库访问
    public Users login(UserLoginDTO userLoginDTO) {
        Users user = usersMapper.selectByUsername(userLoginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        } else if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return user; // 登录成功，用户信息将被缓存
        } else {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    /**
     * 根据用户ID获取用户信息
     * <p>
     * 该方法从缓存或数据库获取用户详细信息
     *
     * @param userId 用户ID
     * @return 用户实体对象
     */
    @Override
    @Cacheable(value = "users", key = "#userId") // 缓存用户信息，提高查询效率
    public Users getUserById(Long userId) {
        return usersMapper.selectById(userId);
    }

    /**
     * 分页查询用户信息
     *
     * @param page 分页参数
     * @return 用户分页数据
     */
    @Override
    public IPage<Users> selectPage(IPage<Users> page) {
        return usersMapper.selectPage(page);
    }

    /**
     * 更新用户信息
     * <p>
     * 该方法更新用户基本信息：
     * 1. 检查用户是否存在
     * 2. 更新用户信息并加密新密码
     * 3. 清除该用户的缓存
     *
     * @param userId  用户ID
     * @param userDTO 用户更新数据传输对象
     * @return 更新成功返回true，失败返回false
     * @throws RuntimeException 用户不存在时抛出异常
     *                          <p>
     *                          注：应考虑添加@Transactional注解确保操作的原子性
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId") // 清除更新用户的缓存
    public boolean updateUserInfo(Long userId, UserDTO userDTO) {
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return usersMapper.updateById(user) > 0;
    }

    /**
     * 删除用户
     * <p>
     * 该方法根据用户ID删除用户，并清除缓存
     *
     * @param userId 用户ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @CacheEvict(value = "users", key = "#userId") // 清除被删除用户的缓存
    public boolean deleteUser(Long userId) {
        return usersMapper.deleteById(userId) > 0;
    }

    /**
     * 根据用户名查询用户
     * <p>
     * 该方法从缓存或数据库获取指定用户名的用户信息，
     * 通常用于用户登录和检查用户名唯一性
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    @Cacheable(value = "users", key = "'username_' + #username")
    public Users getUserByUsername(String username) {
        return usersMapper.selectByUsername(username);
    }

    /**
     * 根据电话号码查询用户
     * <p>
     * 该方法从缓存或数据库获取指定电话号码的用户信息，
     * 通常用于手机号登录或找回密码
     *
     * @param phone 电话号码
     * @return 用户信息
     */
    @Override
    @Cacheable(value = "users", key = "'phone_' + #phone")
    public Users getUserByPhone(String phone) {
        return usersMapper.selectByPhone(phone);
    }

    /**
     * 根据邮箱查询用户
     * <p>
     * 该方法从缓存或数据库获取指定邮箱的用户信息，
     * 通常用于邮箱登录或找回密码
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @Override
    @Cacheable(value = "users", key = "'email_' + #email")
    public Users getUserByEmail(String email) {
        return usersMapper.selectByEmail(email);
    }

    /**
     * 根据状态查询用户列表
     * <p>
     * 该方法查询特定状态的所有用户，
     * 通常用于管理后台的用户管理
     *
     * @param status 用户状态
     * @return 用户列表
     */
    @Override
    public List<Users> getUsersByStatus(Integer status) {
        return usersMapper.selectByStatus(status);
    }

    /**
     * 高级条件查询用户
     * <p>
     * 该方法根据多种条件组合查询用户，并支持分页，
     * 适用于后台管理系统的用户搜索功能
     *
     * @param username 用户名(可选)
     * @param phone    手机号(可选)
     * @param status   状态(可选)
     * @param page     分页参数
     * @return 用户分页数据
     */
    @Override
    public IPage<Users> getUsersByCondition(String username, String phone, Integer status, Page<Users> page) {
        return usersMapper.selectUsersByCondition(username, phone, status, page);
    }

    /**
     * 更新用户状态
     * <p>
     * 该方法用于启用或禁用用户账号，
     * 通常用于管理员对用户的管理操作
     *
     * @param userId 用户ID
     * @param status 新状态
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public boolean updateUserStatus(Long userId, Integer status) {
        return usersMapper.updateUserStatus(userId, status) > 0;
    }

    /**
     * 批量删除用户
     * <p>
     * 该方法批量删除多个用户，
     * 通常用于后台管理系统的批量操作
     *
     * @param userIds 用户ID列表
     * @return 删除结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public boolean batchDeleteUsers(List<Long> userIds) {
        return usersMapper.batchDeleteUsers(userIds) > 0;
    }

    /**
     * 检查用户名是否存在
     * <p>
     * 该方法检查用户名在系统中是否已被使用，
     * 用于用户注册前的唯一性检查
     *
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    @Override
    public boolean checkUsernameExists(String username) {
        return usersMapper.checkUsernameExists(username) > 0;
    }

    /**
     * 重置用户密码
     * <p>
     * 该方法用于管理员重置用户密码或用户找回密码，
     * 不需要验证原密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     * @return 重置结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public boolean resetPassword(Long userId, String newPassword) {
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return usersMapper.updateById(user) > 0;
    }

    /**
     * 用户修改密码
     * <p>
     * 该方法用于用户主动修改密码，
     * 需要验证原密码正确性
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        // 设置新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        return usersMapper.updateById(user) > 0;
    }

    /**
     * 更新用户角色
     * <p>
     * 该方法用于管理员更新用户的角色权限，
     * 通常用于后台用户权限管理
     *
     * @param userId 用户ID
     * @param role   新角色
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public boolean updateUserRole(Long userId, Integer role) {
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 设置新角色
        user.setRole(role);
        return usersMapper.updateById(user) > 0;
    }

    /**
     * 统计用户数量
     * <p>
     * 该方法统计系统中的用户总数或特定状态的用户数，
     * 通常用于管理后台的数据统计
     *
     * @param status 状态(可选)
     * @return 用户数量
     */
    @Override
    public int countUsers(Integer status) {
        if (status == null) {
            return (int) count(); // 强制转换为int类型
        } else {
            return getUsersByStatus(status).size();
        }
    }

    /**
     * 获取用户基本信息（不含敏感信息）
     * <p>
     * 该方法返回用户的基本信息，但不包含密码等敏感数据，
     * 适用于前端展示用户信息的场景
     *
     * @param userId 用户ID
     * @return 用户基本信息
     */
    @Override
    @Cacheable(value = "users", key = "'basic_' + #userId")
    public Users getUserBasicInfo(Long userId) {
        Users user = usersMapper.selectById(userId);
        if (user != null) {
            // 清除敏感信息
            user.setPassword(null);
        }
        return user;
    }

    /**
     * 更新用户头像
     * <p>
     * 该方法用于用户上传或更换头像，
     * 通常在个人中心的头像设置功能中使用
     *
     * @param userId    用户ID
     * @param avatarUrl 头像URL
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true) // 清除该用户所有缓存
    public boolean updateUserAvatar(Long userId, String avatarUrl) {
        Users user = usersMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        user.setAvatar(avatarUrl);
        return usersMapper.updateById(user) > 0;
    }

    /**
     * 更新用户手机号
     * <p>
     * 该方法用于用户修改绑定的手机号，
     * 需要验证码验证，确保操作安全
     *
     * @param userId     用户ID
     * @param newPhone   新手机号
     * @param verifyCode 验证码
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true) // 清除该用户所有缓存
    public boolean updateUserPhone(Long userId, String newPhone, String verifyCode) {
        // 验证码校验逻辑应该在这里实现
        // 这里简化处理，假设验证码已通过验证

        // 检查手机号是否被其他用户使用
        Users existingUser = getUserByPhone(newPhone);
        if (existingUser != null && !existingUser.getId().equals(userId)) {
            return false; // 手机号已被其他用户使用
        }

        Users user = usersMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        user.setPhone(newPhone);
        return usersMapper.updateById(user) > 0;
    }
}




