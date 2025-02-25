package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_behavior(用户行为记录表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:47
 * @Entity model.entity.UserBehavior
 */
@Mapper
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {

    /**
     * 根据用户ID查询行为记录
     *
     * @param userId 用户ID
     * @return 用户行为列表
     */
    List<UserBehavior> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据行为类型查询用户行为
     *
     * @param behaviorType 行为类型
     * @return 用户行为列表
     */
    List<UserBehavior> selectByBehaviorType(@Param("behaviorType") Integer behaviorType);

    /**
     * 根据用户ID和行为类型查询
     *
     * @param userId 用户ID
     * @param behaviorType 行为类型
     * @return 用户行为列表
     */
    List<UserBehavior> selectByUserIdAndType(
            @Param("userId") Long userId, 
            @Param("behaviorType") Integer behaviorType);

    /**
     * 根据时间范围查询用户行为
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户行为列表
     */
    List<UserBehavior> selectByTimeRange(
            @Param("startTime") Date startTime, 
            @Param("endTime") Date endTime);

    /**
     * 查询热门商品
     *
     * @param behaviorType 行为类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 热门商品列表
     */
    List<ProductPopularityVO> selectPopularProducts(
            @Param("behaviorType") Integer behaviorType,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("limit") Integer limit);

    /**
     * 获取用户兴趣分类
     *
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户兴趣分类
     */
    List<UserInterestVO> selectUserInterests(
            @Param("userId") Long userId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    /**
     * 批量插入用户行为
     *
     * @param behaviors 用户行为列表
     * @return 影响行数
     */
    int batchInsertBehaviors(@Param("behaviors") List<UserBehavior> behaviors);

    /**
     * 高级条件查询用户行为
     *
     * @param userId 用户ID（可选）
     * @param productId 商品ID（可选）
     * @param behaviorType 行为类型（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @param page 分页参数
     * @return 分页用户行为
     */
    IPage<UserBehavior> selectBehaviorsByCondition(
            @Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("behaviorType") Integer behaviorType,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            Page<UserBehavior> page);

    /**
     * 统计用户行为数量
     *
     * @param userId 用户ID
     * @param behaviorType 行为类型
     * @return 行为数量
     */
    int countUserBehaviors(
            @Param("userId") Long userId,
            @Param("behaviorType") Integer behaviorType);

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




