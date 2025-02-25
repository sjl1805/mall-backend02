package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.UserMapper;
import com.example.model.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(User user) {
        // 检查用户名是否已存在
        if (findByUsername(user.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 设置默认值
        user.setRole(user.getRole() == null ? 1 : user.getRole()); // 默认普通用户
        user.setStatus(1); // 默认启用
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setLastActiveTime(LocalDateTime.now());
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 保存用户
        save(user);
        
        // 返回用户信息（不返回密码）
        user.setPassword(null);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = findByUsername(username);
        
        // 用户不存在或密码不匹配
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        
        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 更新最后活跃时间
        updateLastActiveTime(user.getId());
        
        // 返回用户信息（不返回密码）
        user.setPassword(null);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return baseMapper.findByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(User user) {
        if (user == null || user.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        
        // 不允许修改的字段设为null
        user.setUsername(null);
        user.setPassword(null);
        user.setRole(null);
        user.setStatus(null);
        user.setCreateTime(null);
        
        user.setUpdateTime(LocalDateTime.now());
        
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        
        // 更新密码
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId)
                .set(User::getPassword, passwordEncoder.encode(newPassword))
                .set(User::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    public IPage<User> pageUsers(Page<User> page, String keyword) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加搜索条件
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(User::getUsername, keyword)
                    .or().like(User::getNickname, keyword)
                    .or().like(User::getPhone, keyword);
        }
        
        queryWrapper.orderByDesc(User::getCreateTime);
        
        return page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserStatus(Long userId, Integer status) {
        if (status != 0 && status != 1) {
            throw new BusinessException("状态参数错误");
        }
        
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId)
                .set(User::getStatus, status)
                .set(User::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLastActiveTime(Long userId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId)
                .set(User::getLastActiveTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }
} 