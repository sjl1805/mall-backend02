package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.exception.BusinessException;
import com.example.model.dto.users.UserLoginDTO;
import com.example.model.dto.users.UserPageDTO;
import com.example.model.dto.users.UserRegisterDTO;
import com.example.model.entity.Users;

import java.util.Map;

/**
 * 用户服务接口
 * 定义业务层方法，包含用户管理核心功能
 * 继承MyBatis Plus的IService接口获得基础CRUD能力
 */
public interface UsersService extends IService<Users> {

    /**
     * 分页查询用户
     */
    IPage<Users> listUsersByPage(UserPageDTO queryDTO);

    /**
     * 用户登录（增强版）
     *
     * @param loginDTO 登录参数
     * @return 包含过滤后的用户信息和访问令牌
     */
    Map<String, Object> login(UserLoginDTO loginDTO);

    /**
     * 用户登出（无参版本）
     *
     * @return 操作结果
     */
    Boolean logout();

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息传输对象
     * @return 包含新用户信息和访问令牌的Map
     * @throws BusinessException 当注册信息不符合要求时抛出
     */
    Map<String, Object> registerUser(UserRegisterDTO registerDTO) throws BusinessException;

    /**
     * 更新用户状态
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 获取用户统计信息
     *
     * @return 包含总用户数、状态分布等统计数据的Map
     * @Cacheable 注解实现缓存优化
     */
    Map<String, Integer> getUserStatistics();

    /**
     * 根据ID获取用户
     */
    Users getById(Long id);

    /**
     * 更新用户
     */
    boolean updateById(Users user);
}
