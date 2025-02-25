package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Product;
import com.example.model.entity.RecommendationResult;
import com.example.model.entity.UserSimilarity;

import java.util.List;

/**
 * 推荐服务接口
 */
public interface RecommendationService extends IService<RecommendationResult> {
    
    /**
     * 获取用户推荐商品
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> getRecommendedProducts(Long userId, Integer limit);
    
    /**
     * 基于用户协同过滤的推荐
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> getUserBasedRecommendations(Long userId, Integer limit);
    
    /**
     * 基于物品协同过滤的推荐
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> getItemBasedRecommendations(Long userId, Integer limit);
    
    /**
     * 基于内容的推荐
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> getContentBasedRecommendations(Long userId, Integer limit);
    
    /**
     * 获取与用户相似的用户
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 相似用户列表
     */
    List<UserSimilarity> getSimilarUsers(Long userId, Integer limit);
    
    /**
     * 获取相似商品
     *
     * @param productId 商品ID
     * @param limit 限制数量
     * @return 相似商品列表
     */
    List<Product> getSimilarProducts(Long productId, Integer limit);
    
    /**
     * 保存推荐结果
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @param algorithmType 算法类型：1-用户协同过滤 2-物品协同过滤 3-内容推荐
     * @param score 推荐得分
     * @return 保存的推荐结果
     */
    RecommendationResult saveRecommendation(Long userId, Long productId, Integer algorithmType, Double score);
    
    /**
     * 更新用户相似度
     *
     * @return 更新数量
     */
    int updateUserSimilarities();
    
    /**
     * 更新商品相似度
     *
     * @return 更新数量
     */
    int updateProductSimilarities();
} 