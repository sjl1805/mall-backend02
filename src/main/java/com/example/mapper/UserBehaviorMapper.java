package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.UserBehavior;
import com.example.model.query.BehaviorQuery;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
* @author 31815
* @description 针对表【user_behavior(用户行为记录表)】的数据库操作Mapper
* @createDate 2025-02-18 23:43:52
* @Entity model.entity.UserBehavior
*/
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {

    /**
     * 分页查询用户行为
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<UserBehavior> selectBehaviorPage(IPage<UserBehavior> page,
                                          @Param("query") BehaviorQuery query);

    /**
     * 统计用户行为权重
     * @param userId 用户ID
     * @return 行为权重统计
     */
    Map<String, Object> calculateUserWeight(@Param("userId") Long userId);

    /**
     * 获取用户最近行为
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 行为列表
     */
    List<UserBehavior> selectRecentBehaviors(@Param("userId") Long userId,
                                          @Param("limit") Integer limit);

    /**
     * 统计行为类型分布
     * @param days 最近天数
     * @return 行为类型统计
     */
    List<Map<String, Object>> countBehaviorDistribution(@Param("days") Integer days);

    /**
     * 检查行为是否存在
     * @param userId 用户ID
     * @param productId 商品ID
     * @param behaviorType 行为类型
     * @return 存在数量
     */
    int checkBehaviorExists(@Param("userId") Long userId,
                          @Param("productId") Long productId,
                          @Param("behaviorType") Integer behaviorType);
}




