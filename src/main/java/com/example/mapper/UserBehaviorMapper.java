package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户行为数据访问层接口
 */
@Mapper
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {
    
    /**
     * 获取用户的行为列表
     *
     * @param userId 用户ID
     * @param behaviorType 行为类型
     * @return 行为列表
     */
    @Select("SELECT * FROM user_behavior WHERE user_id = #{userId} AND behavior_type = #{behaviorType} " +
            "ORDER BY create_time DESC")
    List<UserBehavior> findByUserIdAndType(@Param("userId") Long userId, @Param("behaviorType") Integer behaviorType);
    
    /**
     * 获取商品的行为列表
     *
     * @param productId 商品ID
     * @param behaviorType 行为类型
     * @return 行为列表
     */
    @Select("SELECT * FROM user_behavior WHERE product_id = #{productId} AND behavior_type = #{behaviorType} " +
            "ORDER BY create_time DESC")
    List<UserBehavior> findByProductIdAndType(@Param("productId") Long productId, @Param("behaviorType") Integer behaviorType);
    
    /**
     * 统计用户行为数量
     *
     * @param userId 用户ID
     * @param behaviorType 行为类型
     * @return 行为数量
     */
    @Select("SELECT COUNT(*) FROM user_behavior WHERE user_id = #{userId} AND behavior_type = #{behaviorType}")
    int countByUserIdAndType(@Param("userId") Long userId, @Param("behaviorType") Integer behaviorType);
} 