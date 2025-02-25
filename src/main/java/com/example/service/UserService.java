package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.User;

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
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户，登录失败返回null
     */
    User login(String username, String password);
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);
    
    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(User user);
    
    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param keyword 搜索关键词（用户名、昵称、手机号）
     * @return 分页用户列表
     */
    IPage<User> pageUsers(Page<User> page, String keyword);
    
    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态：0禁用 1启用
     * @return 是否更新成功
     */
    boolean updateUserStatus(Long userId, Integer status);
    
    /**
     * 更新用户最后活跃时间
     *
     * @param userId 用户ID
     * @return 是否更新成功
     */
    boolean updateLastActiveTime(Long userId);
} 