package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.entity.Users;

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
}
