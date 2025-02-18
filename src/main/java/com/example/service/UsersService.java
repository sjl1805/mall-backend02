package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.users.UserPageDTO;
import com.example.model.dto.users.UserLoginDTO;
import com.example.model.dto.users.UserRegisterDTO;
import com.example.model.entity.Users;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UsersService extends IService<Users> {

    /**
     * 分页查询用户
     */
    IPage<Users> listUsersByPage(UserPageDTO queryDTO);

    /**
     * 登录
     */
    Users login(UserLoginDTO loginDTO);

    /**
     * 注册新用户
     */
    Map<String, Object> registerUser(UserRegisterDTO registerDTO);

    /**
     * 更新用户状态
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 获取用户统计信息
     */
    Map<String, Object> getUserStatistics();

    /**
     * 根据ID获取用户
     */
    Users getById(Long id);

    /**
     * 更新用户
     */
    boolean updateById(Users user);
}
