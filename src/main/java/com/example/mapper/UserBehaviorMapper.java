package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.UserBehavior;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_behavior(用户行为记录表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:47
 * @Entity model.entity.UserBehavior
 */
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {

    /**
     * 根据用户ID查询行为记录
     *
     * @param userId 用户ID
     * @return 用户行为列表
     */
    List<UserBehavior> selectByUserId(@Param("userId") Long userId);

    /**
     * 分页查询用户行为
     *
     * @param page 分页信息
     * @return 用户行为列表
     */
    IPage<UserBehavior> selectPage(IPage<UserBehavior> page);

    /**
     * 根据ID查询用户行为
     *
     * @param id 行为记录ID
     * @return 用户行为信息
     */
    UserBehavior selectById(@Param("id") Long id);

    /**
     * 插入新用户行为
     *
     * @param userBehavior 用户行为信息
     * @return 插入结果
     */
    int insertUserBehavior(UserBehavior userBehavior);

    /**
     * 更新用户行为信息
     *
     * @param userBehavior 用户行为信息
     * @return 更新结果
     */
    int updateUserBehavior(UserBehavior userBehavior);

    /**
     * 根据ID删除用户行为
     *
     * @param id 行为记录ID
     * @return 删除结果
     */
    int deleteUserBehavior(@Param("id") Long id);
}




