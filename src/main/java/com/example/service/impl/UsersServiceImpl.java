package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.UsersMapper;
import com.example.model.dto.users.UserLoginDTO;
import com.example.model.dto.users.UserPageDTO;
import com.example.model.dto.users.UserRegisterDTO;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import com.example.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
* @author 31815
* @description 针对表【users(用户表)】的数据库操作Service实现
* @createDate 2025-02-18 23:43:44
*/
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "userService")
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> 
    implements UsersService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()", unless = "#result == null")
    public IPage<Users> listUsersByPage(UserPageDTO queryDTO) {
        Page<Users> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectUserPage(page, queryDTO);
    }

    /**
     * 用户登录实现
     * 1. 根据账号查询有效用户
     * 2. 密码校验
     * 3. 生成JWT令牌
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

    @Override
    public Boolean logout() {
        // 示例实现：记录登出日志或使token失效
        return true; // 根据实际业务逻辑返回操作结果
    }

    /**
     * 用户注册实现
     * 1. 校验用户名唯一性
     * 2. 密码加密处理
     * 3. 保存用户信息
     * 4. 返回注册结果
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
     * 获取用户统计信息
     * 包含：总用户数、各状态用户数、角色分布
     */
    @Override
    @Cacheable(key = "'stats'", cacheNames = "userStats", unless = "#result == null")
    public Map<String, Integer> getUserStatistics() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("totalUsers", (int) count());
        
        List<Map<String, Object>> statusCounts = baseMapper.countUserStatus();
        statusCounts.forEach(item -> 
            stats.put("status_" + item.get("status"), 
                ((Long)item.get("count")).intValue())
        );

        return stats;
    }

    @Override
    @Cacheable(key = "#id", unless = "#result == null")
    public Users getById(Long id) {
        return super.getById(id);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(key = "#user.id"),
        @CacheEvict(cacheNames = "userStats", allEntries = true)
    })
    public boolean updateById(Users user) {
        return super.updateById(user);
    }

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
     * 检查用户名是否存在（缓存优化版）
     */
    private boolean checkUsernameExists(String username) {
        return lambdaQuery()
                .eq(Users::getUsername, username)
                .exists();
    }

    private String getRoleName(Integer roleCode) {
        return switch (roleCode) {
            case 9 -> "ADMIN";
            case 2 -> "MERCHANT";
            default -> "USER";
        };
    }
}




