package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.RecommendationResult;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 推荐结果Mapper接口
 */
@Mapper
public interface RecommendationResultMapper extends BaseMapper<RecommendationResult> {
    
    /**
     * 查询用户的推荐商品列表
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 推荐商品列表
     */
    List<RecommendationResult> selectByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 根据用户ID和算法类型查询推荐结果
     * @param userId 用户ID
     * @param algorithmType 算法类型
     * @param limit 返回数量限制
     * @return 推荐商品列表
     */
    List<RecommendationResult> selectByUserIdAndAlgorithmType(
        @Param("userId") Long userId, 
        @Param("algorithmType") Integer algorithmType, 
        @Param("limit") Integer limit
    );
    
    /**
     * 查询未过期的推荐结果
     * @param userId 用户ID
     * @param currentTime 当前时间
     * @param limit 返回数量限制
     * @return 未过期的推荐商品列表
     */
    List<RecommendationResult> selectValidRecommendations(
        @Param("userId") Long userId, 
        @Param("currentTime") LocalDateTime currentTime, 
        @Param("limit") Integer limit
    );
    
    /**
     * 查询特定商品是否推荐给用户
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 推荐结果，不存在则返回null
     */
    RecommendationResult selectByUserIdAndProductId(
        @Param("userId") Long userId, 
        @Param("productId") Long productId
    );
    
    /**
     * 插入或更新推荐结果
     * @param userId 用户ID
     * @param productId 商品ID
     * @param score 推荐分数
     * @param algorithmType 算法类型
     * @param expireTime 过期时间
     * @return 影响行数
     */
    int insertOrUpdate(
        @Param("userId") Long userId, 
        @Param("productId") Long productId, 
        @Param("score") BigDecimal score, 
        @Param("algorithmType") Integer algorithmType, 
        @Param("expireTime") LocalDateTime expireTime
    );
    
    /**
     * 批量插入推荐结果
     * @param recommendationList 推荐结果列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<RecommendationResult> recommendationList);
    
    /**
     * 删除过期的推荐结果
     * @param currentTime 当前时间
     * @return 影响行数
     */
    int deleteExpiredRecommendations(@Param("currentTime") LocalDateTime currentTime);
    
    /**
     * 删除指定用户的所有推荐结果
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 删除指定算法类型的推荐结果
     * @param algorithmType 算法类型
     * @return 影响行数
     */
    int deleteByAlgorithmType(@Param("algorithmType") Integer algorithmType);
    
    /**
     * 查询推荐分数高于特定值的商品
     * @param userId 用户ID
     * @param minScore 最低分数
     * @param limit 返回数量限制
     * @return 高分推荐商品列表
     */
    List<RecommendationResult> selectHighScoreRecommendations(
        @Param("userId") Long userId, 
        @Param("minScore") BigDecimal minScore, 
        @Param("limit") Integer limit
    );
    
    /**
     * 根据用户最近浏览商品生成的相似商品推荐
     * @param productIds 最近浏览的商品ID列表
     * @param limit 返回数量限制
     * @return 相似商品推荐列表
     */
    @MapKey("productId")
    Map<Long, Map<String, Object>> selectSimilarProductRecommendations(
        @Param("productIds") List<Long> productIds, 
        @Param("limit") Integer limit
    );
    
    /**
     * 分页查询用户的推荐商品
     * @param page 分页参数
     * @param userId 用户ID
     * @return 分页数据
     */
    IPage<RecommendationResult> selectPageByUserId(Page<RecommendationResult> page, @Param("userId") Long userId);
    
    /**
     * 查询算法类型分布统计
     * @return 各算法类型的推荐结果数量
     */
    List<Map<String, Object>> selectAlgorithmTypeStats();
} 