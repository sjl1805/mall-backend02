package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.exception.BusinessException;
import com.example.model.dto.users.UserLoginDTO;
import com.example.model.dto.users.UserPageDTO;
import com.example.model.dto.users.UserRegisterDTO;
import com.example.model.entity.Users;
import com.example.model.dto.users.UserDTO;

import java.util.Map;

/**
 * 用户服务接口
 * 
 * @author 31815
 * @description 提供用户全生命周期管理功能，包含：
 *              1. 用户认证与授权
 *              2. 用户信息管理
 *              3. 用户状态控制
 * @createDate 2025-02-18 23:43:44
 */
public interface UsersService extends IService<Users> {

    /**
     * 分页查询用户（带缓存）
     * @param queryDTO 分页参数：
     *                 - keyword: 用户名/手机号搜索
     *                 - role: 角色过滤
     *                 - status: 状态过滤
     * @return 分页结果（包含用户基本信息）
     * @implNote 结果缓存优化，有效期15分钟
     */
    IPage<UserDTO> listUsersByPage(UserPageDTO queryDTO);

    /**
     * 用户登录（增强版）
     * @param loginDTO 登录参数：
     *                 - username: 用户名/手机号
     *                 - password: 密码（需加密传输）
     * @return 包含用户基本信息和访问令牌的Map
     * @throws BusinessException 当用户不存在或密码错误时抛出
     */
    Map<String, Object> login(UserLoginDTO loginDTO);

    /**
     * 用户登出（无参版本）
     * @return 操作结果
     * @implNote 需配合前端清除本地存储的token
     */
    Boolean logout();

    /**
     * 用户注册（带校验）
     * @param registerDTO 注册信息：
     *                   - username: 用户名（必须）
     *                   - password: 密码（需符合复杂度要求）
     *                   - phone: 手机号（需符合格式）
     * @return 包含新用户ID和访问令牌的Map
     * @throws BusinessException 当注册信息不符合要求时抛出
     */
    Map<String, Object> registerUser(UserRegisterDTO registerDTO) throws BusinessException;

    /**
     * 更新用户状态（管理端）
     * @param userId 用户ID
     * @param status 新状态（0-禁用，1-启用）
     * @return 操作是否成功
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 获取用户统计信息（带缓存）
     * @return 包含总用户数、状态分布等统计数据的Map
     * @implNote 结果缓存优化，有效期2小时
     */
    Map<String, Integer> getUserStatistics();

    /**
     * 根据ID获取用户（带缓存）
     * @param id 用户ID
     * @return 用户详细信息
     * @implNote 结果缓存优化，有效期30分钟
     */
    UserDTO getById(Long id);

    /**
     * 更新用户（带缓存清除）
     * @param user 用户信息（必须包含ID）
     * @return 操作是否成功
     */
    boolean updateById(UserDTO user);
}
