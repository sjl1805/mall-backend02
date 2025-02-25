package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserBehavior;
import com.example.model.entity.Products;
import com.example.model.entity.Users;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【user_behavior(用户行为记录表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:47
 */
public interface UserBehaviorService extends IService<UserBehavior> {

    /**
     * 根据用户ID查询行为记录
     *
     * @param userId 用户ID
     * @return 用户行为记录列表
     */
    List<UserBehavior> selectByUserId(Long userId);

    /**
     * 分页查询用户行为记录
     *
     * @param page 分页信息
     * @return 用户行为记录列表
     */
    IPage<UserBehavior> selectPage(IPage<UserBehavior> page);

    /**
     * 根据ID查询用户行为记录
     *
     * @param id 行为记录ID
     * @return 用户行为记录信息
     */
    UserBehavior selectById(Long id);

    /**
     * 新增用户行为记录
     *
     * @param userBehavior 用户行为记录信息
     * @return 插入结果
     */
    boolean insertUserBehavior(UserBehavior userBehavior);

    /**
     * 更新用户行为记录信息
     *
     * @param userBehavior 用户行为记录信息
     * @return 更新结果
     */
    boolean updateUserBehavior(UserBehavior userBehavior);

    /**
     * 根据ID删除用户行为记录
     *
     * @param id 行为记录ID
     * @return 删除结果
     */
    boolean deleteUserBehavior(Long id);

    /**
     * 根据行为类型查询用户行为
     *
     * @param behaviorType 行为类型
     * @return 用户行为列表
     */
    List<UserBehavior> selectByBehaviorType(Integer behaviorType);

    /**
     * 根据用户ID和行为类型查询
     *
     * @param userId 用户ID
     * @param behaviorType 行为类型
     * @return 用户行为列表
     */
    List<UserBehavior> selectByUserIdAndType(Long userId, Integer behaviorType);

    /**
     * 根据时间范围查询用户行为
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户行为列表
     */
    List<UserBehavior> selectByTimeRange(Date startTime, Date endTime);

    /**
     * 查询热门商品
     *
     * @param behaviorType 行为类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 热门商品列表
     */
    List<Products> selectPopularProducts(
            Integer behaviorType, Date startTime, Date endTime, Integer limit);

    /**
     * 获取用户兴趣分类
     *
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户兴趣分类
     */
    List<Users> selectUserInterests(Long userId, Date startTime, Date endTime);

    /**
     * 批量插入用户行为
     *
     * @param behaviors 用户行为列表
     * @return 插入结果
     */
    boolean batchInsertBehaviors(List<UserBehavior> behaviors);

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
            Long userId, Long productId, Integer behaviorType,
            Date startTime, Date endTime, Page<UserBehavior> page);

    /**
     * 统计用户行为数量
     *
     * @param userId 用户ID
     * @param behaviorType 行为类型
     * @return 行为数量
     */
    int countUserBehaviors(Long userId, Integer behaviorType);

    /**
     * 分析用户行为时间分布
     * 按小时统计用户行为频率
     *
     * @param userId 用户ID
     * @param behaviorType 行为类型
     * @return 行为时间分布
     */
    Map<Integer, Integer> analyzeUserBehaviorTimeDistribution(Long userId, Integer behaviorType);

    /**
     * 分析用户行为路径
     * 跟踪用户从浏览到购买的行为路径
     *
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户行为路径
     */
    List<Map<String, Object>> analyzeUserBehaviorPath(Long userId, Date startTime, Date endTime);

    /**
     * 计算用户活跃度
     * 基于行为频率、多样性、权重等因素
     *
     * @param userId 用户ID
     * @param days 天数，计算最近几天的活跃度
     * @return 活跃度得分
     */
    BigDecimal calculateUserActivityScore(Long userId, Integer days);

    /**
     * 预测用户下一步可能的行为
     *
     * @param userId 用户ID
     * @return 可能的行为类型及概率
     */
    Map<Integer, BigDecimal> predictNextBehavior(Long userId);

    /**
     * 识别用户异常行为
     * 检测与用户正常行为模式偏离较大的行为
     *
     * @param userId 用户ID
     * @param days 天数，检测最近几天的行为
     * @return 异常行为列表
     */
    List<UserBehavior> detectAbnormalBehaviors(Long userId, Integer days);

    /**
     * 分析用户行为转化
     * 计算从一种行为到另一种行为的转化率
     *
     * @param sourceType 源行为类型
     * @param targetType 目标行为类型
     * @param days 天数
     * @return 转化率
     */
    BigDecimal analyzeBehaviorConversion(Integer sourceType, Integer targetType, Integer days);
}
