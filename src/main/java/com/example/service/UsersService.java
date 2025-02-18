package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.UserQuery;
import com.example.model.entity.Users;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UsersService extends IService<Users> {

    /**
     * 分页查询用户
     */
    IPage<Users> listUsersByPage(UserQuery query);

    /**
     * 注册新用户
     */
    Users registerUser(Users user);

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
