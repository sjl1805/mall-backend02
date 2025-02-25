package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.RecommendationResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 推荐结果数据访问层接口
 */
@Mapper
public interface RecommendationResultMapper extends BaseMapper<RecommendationResult> {
    
    /**
     * 获取用户的推荐商品列表
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐结果列表
     */
    @Select("SELECT * FROM recommendation_result WHERE user_id = #{userId} " +
            "AND (expire_time IS NULL OR expire_time > NOW()) " +
            "ORDER BY score DESC LIMIT #{limit}")
    List<RecommendationResult> findByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 根据算法类型获取用户的推荐商品列表
     *
     * @param userId 用户ID
     * @param algorithmType 算法类型
     * @param limit 限制数量
     * @return 推荐结果列表
     */
    @Select("SELECT * FROM recommendation_result WHERE user_id = #{userId} " +
            "AND algorithm_type = #{algorithmType} " +
            "AND (expire_time IS NULL OR expire_time > NOW()) " +
            "ORDER BY score DESC LIMIT #{limit}")
    List<RecommendationResult> findByUserIdAndAlgorithmType(
            @Param("userId") Long userId, 
            @Param("algorithmType") Integer algorithmType, 
            @Param("limit") Integer limit);
} 