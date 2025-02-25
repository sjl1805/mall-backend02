package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.UserMapper;
import com.example.model.entity.User;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Value("${spring.password.encoder-strength}")
    private int strength;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(User user) {
        // 验证用户信息
        validateUserForRegister(user);

        // 设置默认值
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(1); // 默认普通用户
        user.setStatus(1); // 默认启用
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setLastActiveTime(LocalDateTime.now());

        // 保存用户
        boolean success = save(user);
        if (!success) {
            throw new BusinessException(ResultCode.USER_REGISTER_FAILED);
        }

        // 不返回密码
        user.setPassword(null);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = null;

        // 尝试使用用户名、手机号或邮箱登录
        if (username.contains("@")) {
            user = userMapper.selectByEmail(username);
        } else if (username.matches("^[1][3-9][0-9]{9}$")) {
            user = userMapper.selectByPhone(username);
        } else {
            user = userMapper.selectByUsername(username);
        }

        // 验证用户和密码
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_LOCKED);
        }

        // 更新最后登录时间
        updateLastActiveTime(user.getId());

        // 不返回密码
        user.setPassword(null);
        return user;
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public User getByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(User user) {
        if (user.getId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户ID不能为空");
        }

        User existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 不允许修改用户名、密码和角色
        user.setUsername(null);
        user.setPassword(null);
        user.setRole(null);
        user.setUpdateTime(LocalDateTime.now());

        boolean success = updateById(user);
        if (!success) {
            throw new BusinessException(ResultCode.USER_UPDATE_FAILED);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR, "原密码错误");
        }

        // 加密新密码
        String encodedPassword = passwordEncoder.encode(newPassword);
        int rows = userMapper.updatePassword(userId, encodedPassword);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long userId, Integer status) {
        if (!List.of(0, 1).contains(status)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "状态值无效");
        }

        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        int rows = userMapper.updateStatus(userId, status);
        return rows > 0;
    }

    @Override
    public boolean updateLastActiveTime(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        int rows = userMapper.updateLastActiveTime(userId);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserTags(Long userId, List<Map<String, Object>> tags) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        try {
            String tagsJson = objectMapper.writeValueAsString(tags);
            int rows = userMapper.updateUserTags(userId, tagsJson);
            return rows > 0;
        } catch (JsonProcessingException e) {
            log.error("更新用户标签失败", e);
            throw new BusinessException(ResultCode.DATA_ERROR, "标签数据格式错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPreferredCategories(Long userId, List<Long> preferredCategories) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        try {
            String categoriesJson = objectMapper.writeValueAsString(preferredCategories);
            int rows = userMapper.updateUserPreferredCategories(userId, categoriesJson);
            return rows > 0;
        } catch (JsonProcessingException e) {
            log.error("更新用户偏好分类失败", e);
            throw new BusinessException(ResultCode.DATA_ERROR, "偏好分类数据格式错误");
        }
    }

    @Override
    public IPage<User> getUserPage(Page<User> page, Map<String, Object> params) {
        return userMapper.selectUserPage(page, params);
    }

    @Override
    public List<User> getBatchUsersByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        return userMapper.selectBatchByIds(userIds);
    }

    @Override
    public List<Map<String, Object>> countUserByGender() {
        return userMapper.countUserByGender();
    }

    @Override
    public List<Map<String, Object>> countUserByAgeRange() {
        return userMapper.countUserByAgeRange();
    }

    @Override
    public List<Map<String, Object>> countUserByConsumptionLevel() {
        return userMapper.countUserByConsumptionLevel();
    }

    @Override
    public List<Map<String, Object>> countUserGrowth(Integer days) {
        if (days == null || days <= 0) {
            days = 30; // 默认30天
        }
        return userMapper.countUserGrowth(days);
    }

    @Override
    public List<User> getActiveUsers(Integer days, Integer limit) {
        if (days == null || days <= 0) {
            days = 7; // 默认7天
        }
        if (limit == null || limit <= 0) {
            limit = 100; // 默认最多100个
        }
        return userMapper.selectActiveUsers(days, limit);
    }

    @Override
    public boolean checkUsernameExists(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        User user = userMapper.selectByUsername(username);
        return user != null;
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        User user = userMapper.selectByPhone(phone);
        return user != null;
    }

    @Override
    public boolean checkEmailExists(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        User user = userMapper.selectByEmail(email);
        return user != null;
    }

    /**
     * 验证用户注册信息
     *
     * @param user 用户信息
     */
    private void validateUserForRegister(User user) {
        if (user == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户信息不能为空");
        }

        if (!StringUtils.hasText(user.getUsername())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户名不能为空");
        }

        if (!StringUtils.hasText(user.getPassword())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "密码不能为空");
        }

        // 检查用户名是否已存在
        if (checkUsernameExists(user.getUsername())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "用户名已存在");
        }

        // 检查手机号是否已存在
        if (StringUtils.hasText(user.getPhone()) && checkPhoneExists(user.getPhone())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "手机号已注册");
        }

        // 检查邮箱是否已存在
        if (StringUtils.hasText(user.getEmail()) && checkEmailExists(user.getEmail())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "邮箱已注册");
        }
    }
} 