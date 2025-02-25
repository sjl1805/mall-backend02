package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.RecommendationResult;
import com.example.model.vo.ProductRecommendVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 推荐结果服务接口
 */
public interface RecommendationResultService extends IService<RecommendationResult> {
    
    /**
     * 获取用户的推荐商品列表
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 推荐商品列表
     */
    List<RecommendationResult> getUserRecommendations(Long userId, Integer limit);
    
    /**
     * 根据用户ID和算法类型获取推荐结果
     * @param userId 用户ID
     * @param algorithmType 算法类型
     * @param limit 返回数量限制
     * @return 推荐商品列表
     */
    List<RecommendationResult> getRecommendationsByAlgorithmType(Long userId, Integer algorithmType, Integer limit);
    
    /**
     * 获取未过期的推荐结果
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 未过期的推荐商品列表
     */
    List<RecommendationResult> getValidRecommendations(Long userId, Integer limit);
    
    /**
     * 检查特定商品是否推荐给用户
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 推荐结果，不存在则返回null
     */
    RecommendationResult checkProductRecommended(Long userId, Long productId);
    
    /**
     * 保存或更新推荐结果
     * @param userId 用户ID
     * @param productId 商品ID
     * @param score 推荐分数
     * @param algorithmType 算法类型
     * @param expireTime 过期时间
     * @return 是否成功
     */
    boolean saveOrUpdateRecommendation(Long userId, Long productId, BigDecimal score, Integer algorithmType, LocalDateTime expireTime);
    
    /**
     * 批量保存推荐结果
     * @param recommendationList 推荐结果列表
     * @return 是否成功
     */
    boolean batchSaveRecommendations(List<RecommendationResult> recommendationList);
    
    /**
     * 清理过期的推荐结果
     * @return 清理的记录数
     */
    int cleanExpiredRecommendations();
    
    /**
     * 删除指定用户的所有推荐结果
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteUserRecommendations(Long userId);
    
    /**
     * 删除指定算法类型的推荐结果
     * @param algorithmType 算法类型
     * @return 是否成功
     */
    boolean deleteRecommendationsByAlgorithmType(Integer algorithmType);
    
    /**
     * 获取推荐分数高于特定值的商品
     * @param userId 用户ID
     * @param minScore 最低分数
     * @param limit 返回数量限制
     * @return 高分推荐商品列表
     */
    List<RecommendationResult> getHighScoreRecommendations(Long userId, BigDecimal minScore, Integer limit);
    
    /**
     * 根据用户最近浏览商品生成的相似商品推荐
     * @param productIds 最近浏览的商品ID列表
     * @param limit 返回数量限表
     * @return 相似商品推荐列表
     */
    List<Map<String, Object>> getSimilarProductRecommendations(List<Long> productIds, Integer limit);
    
    /**
     * 分页查询用户的推荐商品
     * @param page 分页参数
     * @param userId 用户ID
     * @return 分页数据
     */
    IPage<RecommendationResult> getUserRecommendationsPage(Page<RecommendationResult> page, Long userId);
    
    /**
     * 获取算法类型分布统计
     * @return 各算法类型的推荐结果数量
     */
    List<Map<String, Object>> getAlgorithmTypeStats();
    
    /**
     * 基于用户行为生成个性化推荐
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 是否成功
     */
    boolean generatePersonalizedRecommendations(Long userId, Integer limit);
    
    /**
     * 基于用户相似度生成推荐
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 是否成功
     */
    boolean generateUserBasedRecommendations(Long userId, Integer limit);
    
    /**
     * 基于商品相似度生成推荐
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 是否成功
     */
    boolean generateItemBasedRecommendations(Long userId, Integer limit);
    
    /**
     * 生成热门商品推荐
     * @param limit 返回数量限制
     * @return 是否成功
     */
    boolean generatePopularRecommendations(Integer limit);
    
    /**
     * 生成新品推荐
     * @param days 最近天数
     * @param limit 返回数量限制
     * @return 是否成功
     */
    boolean generateNewProductRecommendations(Integer days, Integer limit);
    
    /**
     * 获取用户推荐商品详情
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 推荐商品详情列表
     */
    List<ProductRecommendVO> getUserRecommendProductDetails(Long userId, Integer limit);
    
    /**
     * 更新推荐结果的过期时间
     * @param id 推荐结果ID
     * @param expireTime 新的过期时间
     * @return 是否成功
     */
    boolean updateRecommendationExpireTime(Long id, LocalDateTime expireTime);
    
    /**
     * 获取推荐结果的有效期统计
     * @return 有效期统计数据
     */
    Map<String, Object> getRecommendationExpiryStats();
} 