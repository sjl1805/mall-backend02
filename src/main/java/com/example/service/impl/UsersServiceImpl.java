package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.UsersMapper;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import com.example.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 * 
 * @author 31815
 * @description 实现用户核心业务逻辑，包含：
 *              1. 安全认证实现
 *              2. 缓存策略优化
 *              3. 状态管理控制
 * @createDate 2025-02-18 23:43:44
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "userService")
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * 分页查询用户（缓存优化）
     * @param queryDTO 分页参数
     * @return 分页结果
     * @implNote 缓存策略：
     *           1. 缓存键：page:{queryDTO.hashCode}
     *           2. 缓存时间：15分钟
     */
    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()", unless = "#result == null")
    public IPage<UserDTO> listUsersByPage(PageDTO<UserDTO> queryDTO) {
        Page<Users> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        IPage<Users> usersPage = baseMapper.selectUserPage(page, queryDTO.getQuery());
        return usersPage.convert(UserDTO::fromEntity);
    }

    /**
     * 用户登录实现
     * @param loginDTO 登录参数
     * @return 用户信息和令牌
     * @implNote 执行逻辑：
     *           1. 查询有效用户
     *           2. 密码校验
     *           3. 生成JWT令牌
     */
    @Override
    public Map<String, Object> login(UserLoginDTO loginDTO) {
        Users user = baseMapper.selectByUsernameOrPhone(loginDTO.getUsername());
        if (user == null || user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        Map<String, Object> userInfo = new LinkedHashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("role", getRoleName(user.getRole()));

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());

        return Map.of(
                "userInfo", userInfo,
                "token", token
        );
    }

    /**
     * 用户注册实现
     * @param registerDTO 注册信息
     * @return 注册结果
     * @implNote 业务逻辑：
     *           1. 校验手机号格式
     *           2. 检查用户名唯一性
     *           3. 密码加密存储
     *           4. 清除统计缓存
     */
    @Override
    @CacheEvict(cacheNames = {"userStats"}, allEntries = true)
    public Map<String, Object> registerUser(UserRegisterDTO registerDTO) {
        if (!registerDTO.getPhone().matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException(ResultCode.INVALID_PHONE_FORMAT);
        }

        if (checkUsernameExists(registerDTO.getUsername())) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }

        Users newUser = new Users();
        newUser.setUsername(registerDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        newUser.setPhone(registerDTO.getPhone());
        newUser.setRole(1);
        newUser.setStatus(1);

        if (!save(newUser)) {
            throw new BusinessException(ResultCode.REGISTER_ERROR);
        }

        String token = jwtUtils.generateToken(newUser.getUsername(), newUser.getRole());

        return Map.of(
                "userId", newUser.getId(),
                "username", newUser.getUsername(),
                "token", token
        );
    }

    /**
     * 更新用户状态（带缓存清除）
     * @param userId 用户ID
     * @param status 新状态
     * @return 操作结果
     * @implNote 清除用户缓存和统计缓存
     */
    @Override
    @Caching(evict = {
            @CacheEvict(key = "#userId"),
            @CacheEvict(cacheNames = "userStats", allEntries = true)
    })
    public boolean updateUserStatus(Long userId, Integer status) {
        return lambdaUpdate()
                .eq(Users::getId, userId)
                .set(Users::getStatus, status)
                .update();
    }

    /**
     * 获取用户统计信息（缓存优化）
     * @return 统计结果
     * @implNote 缓存策略：
     *           1. 缓存键：stats
     *           2. 缓存时间：2小时
     */
    @Override
    @Cacheable(key = "'stats'", cacheNames = "userStats", unless = "#result == null")
    public Map<String, Integer> getUserStatistics() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("totalUsers", (int) count());

        List<Map<String, Object>> statusCounts = baseMapper.countUserStatus();
        statusCounts.forEach(item ->
                stats.put("status_" + item.get("status"),
                        ((Long) item.get("count")).intValue())
        );

        return stats;
    }

    /**
     * 根据ID获取用户（缓存优化）
     * @param id 用户ID
     * @return 用户信息
     * @implNote 缓存策略：
     *           1. 缓存键：用户ID
     *           2. 缓存时间：30分钟
     */
    @Override
    @Cacheable(key = "#id", unless = "#result == null")
    public UserDTO getById(Long id) {
        Users user = super.getById(id);
        return UserDTO.fromEntity(user);
    }

    /**
     * 更新用户（带缓存清除）
     * @param user 用户信息
     * @return 操作结果
     * @implNote 清除用户缓存和统计缓存
     */
    @Override
    @Caching(evict = {
            @CacheEvict(key = "#user.id"),
            @CacheEvict(cacheNames = "userStats", allEntries = true)
    })
    public boolean updateById(UserDTO userDTO) {
        Users user = new Users();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setNickname(userDTO.getNickname());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setAvatar(userDTO.getAvatar());
        user.setGender(userDTO.getGender());
        user.setStatus(userDTO.getStatus());
        user.setRole(userDTO.getRole());
        
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        return super.updateById(user);
    }

    /**
     * Spring Security用户加载实现
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = baseMapper.selectByUsernameOrPhone(username);
        if (user == null || user.getStatus() == 0) {
            throw new UsernameNotFoundException("用户不存在或已被禁用");
        }
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(getRoleName(user.getRole()))
                .build();
    }

    /**
     * 检查用户名唯一性（缓存优化）
     */
    private boolean checkUsernameExists(String username) {
        return lambdaQuery()
                .eq(Users::getUsername, username)
                .exists();
    }

    /**
     * 角色代码转角色名称
     */
    private String getRoleName(Integer roleCode) {
        return switch (roleCode) {
            case 9 -> "ADMIN";
            case 2 -> "MERCHANT";
            default -> "USER";
        };
    }

    @Override
    @CacheEvict(key = "#username", cacheNames = "userService")
    public Boolean logout() {
        SecurityContextHolder.clearContext();
        return true;
    }
}




