package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 注册成功的用户
     */
    User register(User user);

    /**
     * 用户登录
     *
     * @param username 用户名/手机号/邮箱
     * @param password 密码
     * @return 登录用户信息
     */
    User login(String username, String password);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User getByPhone(String phone);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User getByEmail(String email);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(User user);

    /**
     * 更新用户密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否更新成功
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long userId, Integer status);

    /**
     * 更新用户最后活跃时间
     *
     * @param userId 用户ID
     * @return 是否更新成功
     */
    boolean updateLastActiveTime(Long userId);

    /**
     * 更新用户标签
     *
     * @param userId 用户ID
     * @param tags 标签列表
     * @return 是否更新成功
     */
    boolean updateUserTags(Long userId, List<Map<String, Object>> tags);

    /**
     * 更新用户偏好分类
     *
     * @param userId 用户ID
     * @param preferredCategories 偏好分类列表
     * @return 是否更新成功
     */
    boolean updateUserPreferredCategories(Long userId, List<Long> preferredCategories);

    /**
     * 分页查询用户列表
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页用户列表
     */
    IPage<User> getUserPage(Page<User> page, Map<String, Object> params);

    /**
     * 批量获取用户信息
     *
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    List<User> getBatchUsersByIds(List<Long> userIds);

    /**
     * 统计不同性别的用户数量
     *
     * @return 统计结果
     */
    List<Map<String, Object>> countUserByGender();

    /**
     * 统计不同年龄段的用户数量
     *
     * @return 统计结果
     */
    List<Map<String, Object>> countUserByAgeRange();

    /**
     * 统计不同消费能力的用户数量
     *
     * @return 统计结果
     */
    List<Map<String, Object>> countUserByConsumptionLevel();

    /**
     * 统计用户近期增长数据
     *
     * @param days 天数
     * @return 统计结果
     */
    List<Map<String, Object>> countUserGrowth(Integer days);

    /**
     * 获取活跃用户列表
     *
     * @param days 活跃天数
     * @param limit 限制数量
     * @return 活跃用户列表
     */
    List<User> getActiveUsers(Integer days, Integer limit);
    
    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean checkUsernameExists(String username);
    
    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean checkPhoneExists(String phone);
    
    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean checkEmailExists(String email);
} 