package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.users.UserPageDTO;
import com.example.model.dto.users.UserLoginDTO;
import com.example.model.dto.users.UserRegisterDTO;
import com.example.model.entity.Users;
import com.example.exception.BusinessException;
import org.springframework.cache.annotation.Cacheable;

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
     * 用户登录认证
     * @param loginDTO 登录传输对象（包含账号和密码）
     * @return 包含用户信息和访问令牌的Map
     * @throws BusinessException 当认证失败时抛出
     */
    Map<String, Object> login(UserLoginDTO loginDTO) throws BusinessException;

    /*
     * 登出
     */
    Boolean  logout (Long userId);

    /**
     * 用户注册
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
     * @return 包含总用户数、状态分布等统计数据的Map
     * @Cacheable 注解实现缓存优化
     */
    @Cacheable(key = "'userStats'")
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
