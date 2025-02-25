package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UsersMapper;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现类
 * 
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
     * 
     * 该方法执行用户注册流程：
     * 1. 检查用户名是否已存在
     * 2. 如果不存在，创建新用户并加密密码
     * 3. 将用户信息保存到数据库
     *
     * @param userRegisterDTO 用户注册数据传输对象，包含用户名和密码
     * @return 注册成功返回true，失败返回false
     * 
     * 注：应考虑添加@Transactional注解确保操作的原子性
     */
    @Override
    @Transactional
    public boolean register(UserRegisterDTO userRegisterDTO) {
        // 检查用户是否已存在
        Users existingUser = usersMapper.selectByUsername(userRegisterDTO.getUsername());
        if (existingUser != null) {
            return false; // 用户已存在
        }
        // 创建新用户
        Users newUser = new Users();
        newUser.setUsername(userRegisterDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword())); // 密码使用BCrypt加密存储
        return usersMapper.insert(newUser) > 0;
    }

    /**
     * 用户登录
     * 
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
     * 
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
     * 
     * 该方法更新用户基本信息：
     * 1. 检查用户是否存在
     * 2. 更新用户信息并加密新密码
     * 3. 清除该用户的缓存
     *
     * @param userId 用户ID
     * @param userDTO 用户更新数据传输对象
     * @return 更新成功返回true，失败返回false
     * @throws RuntimeException 用户不存在时抛出异常
     * 
     * 注：应考虑添加@Transactional注解确保操作的原子性
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
     * 
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
}




