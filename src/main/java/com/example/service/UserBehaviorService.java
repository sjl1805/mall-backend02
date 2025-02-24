package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserBehavior;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_behavior(用户行为记录表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:47
 */
public interface UserBehaviorService extends IService<UserBehavior> {

    /**
     * 根据用户ID查询行为记录
     * @param userId 用户ID
     * @return 用户行为记录列表
     */
    List<UserBehavior> selectByUserId(Long userId);

    /**
     * 分页查询用户行为记录
     * @param page 分页信息
     * @return 用户行为记录列表
     */
    IPage<UserBehavior> selectPage(IPage<UserBehavior> page);

    /**
     * 根据ID查询用户行为记录
     * @param id 行为记录ID
     * @return 用户行为记录信息
     */
    UserBehavior selectById(Long id);

    /**
     * 新增用户行为记录
     * @param userBehavior 用户行为记录信息
     * @return 插入结果
     */
    boolean insertUserBehavior(UserBehavior userBehavior);

    /**
     * 更新用户行为记录信息
     * @param userBehavior 用户行为记录信息
     * @return 更新结果
     */
    boolean updateUserBehavior(UserBehavior userBehavior);

    /**
     * 根据ID删除用户行为记录
     * @param id 行为记录ID
     * @return 删除结果
     */
    boolean deleteUserBehavior(Long id);
}
