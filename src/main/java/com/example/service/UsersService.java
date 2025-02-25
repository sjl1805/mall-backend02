package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.entity.Users;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【users(用户表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:41
 */
public interface UsersService extends IService<Users> {

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册信息
     * @return 注册结果
     */
    boolean register(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 登录结果
     */
    Users login(UserLoginDTO userLoginDTO);

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    Users getUserById(Long userId);

    /**
     * 分页查询用户
     *
     * @param page 分页信息
     * @return 用户列表
     */
    IPage<Users> selectPage(IPage<Users> page);

    /**
     * 更新用户信息
     *
     * @param userId  用户ID
     * @param userDTO 用户信息
     * @return 更新结果
     */
    boolean updateUserInfo(Long userId, UserDTO userDTO);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteUser(Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    Users getUserByUsername(String username);

    /**
     * 根据电话号码查询用户
     *
     * @param phone 电话号码
     * @return 用户信息
     */
    Users getUserByPhone(String phone);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    Users getUserByEmail(String email);

    /**
     * 根据状态查询用户列表
     *
     * @param status 用户状态
     * @return 用户列表
     */
    List<Users> getUsersByStatus(Integer status);

    /**
     * 高级条件查询用户
     *
     * @param username 用户名(可选)
     * @param phone 手机号(可选)
     * @param status 状态(可选)
     * @param page 分页参数
     * @return 用户分页数据
     */
    IPage<Users> getUsersByCondition(String username, String phone, Integer status, Page<Users> page);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 新状态
     * @return 更新结果
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 批量删除用户
     *
     * @param userIds 用户ID列表
     * @return 删除结果
     */
    boolean batchDeleteUsers(List<Long> userIds);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    boolean checkUsernameExists(String username);

    /**
     * 重置用户密码
     *
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 重置结果
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 用户修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新用户角色
     *
     * @param userId 用户ID
     * @param role 新角色
     * @return 更新结果
     */
    boolean updateUserRole(Long userId, Integer role);

    /**
     * 统计用户数量
     *
     * @param status 状态(可选)
     * @return 用户数量
     */
    int countUsers(Integer status);

    /**
     * 获取用户基本信息（不含敏感信息）
     *
     * @param userId 用户ID
     * @return 用户基本信息
     */
    Users getUserBasicInfo(Long userId);

    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param avatarUrl 头像URL
     * @return 更新结果
     */
    boolean updateUserAvatar(Long userId, String avatarUrl);

    /**
     * 更新用户手机号
     *
     * @param userId 用户ID
     * @param newPhone 新手机号
     * @param verifyCode 验证码
     * @return 更新结果
     */
    boolean updateUserPhone(Long userId, String newPhone, String verifyCode);
}