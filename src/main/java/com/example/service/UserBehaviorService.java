package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserBehavior;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户行为服务接口
 */
public interface UserBehaviorService extends IService<UserBehavior> {
    
    /**
     * 记录用户浏览行为
     * @param userId 用户ID
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @param stayTime 停留时间(秒)
     * @return 是否成功
     */
    boolean recordViewBehavior(Long userId, Long productId, Long categoryId, Integer stayTime);
    
    /**
     * 记录用户点击行为
     * @param userId 用户ID
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @return 是否成功
     */
    boolean recordClickBehavior(Long userId, Long productId, Long categoryId);
    
    /**
     * 记录用户加入购物车行为
     * @param userId 用户ID
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @return 是否成功
     */
    boolean recordCartBehavior(Long userId, Long productId, Long categoryId);
    
    /**
     * 记录用户收藏行为
     * @param userId 用户ID
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @return 是否成功
     */
    boolean recordFavoriteBehavior(Long userId, Long productId, Long categoryId);
    
    /**
     * 记录用户搜索行为
     * @param userId 用户ID
     * @param keyword 搜索关键词
     * @return 是否成功
     */
    boolean recordSearchBehavior(Long userId, String keyword);
    
    /**
     * 记录用户评分行为
     * @param userId 用户ID
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @param rating 评分
     * @return 是否成功
     */
    boolean recordRatingBehavior(Long userId, Long productId, Long categoryId, BigDecimal rating);
    
    /**
     * 记录用户评价行为
     * @param userId 用户ID
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @param rating 评分
     * @param reviewContent 评价内容
     * @return 是否成功
     */
    boolean recordReviewBehavior(Long userId, Long productId, Long categoryId, BigDecimal rating, String reviewContent);
    
    /**
     * 记录用户购买行为
     * @param userId 用户ID
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @return 是否成功
     */
    boolean recordPurchaseBehavior(Long userId, Long productId, Long categoryId);
    
    /**
     * 获取用户行为历史
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 用户行为历史
     */
    List<UserBehavior> getUserBehaviorHistory(Long userId, Integer limit);
    
    /**
     * 获取用户最近浏览的商品
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近浏览的商品ID列表
     */
    List<Long> getRecentViewedProducts(Long userId, Integer limit);
    
    /**
     * 获取用户收藏的商品
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 收藏的商品ID列表
     */
    List<Long> getFavoriteProducts(Long userId, Integer limit);
    
    /**
     * 获取用户的搜索历史
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 搜索关键词列表
     */
    List<String> getUserSearchHistory(Long userId, Integer limit);
    
    /**
     * 获取热门搜索关键词
     * @param limit 限制数量
     * @return 热门关键词列表
     */
    List<Map<String, Object>> getHotSearchKeywords(Integer limit);
    
    /**
     * 获取商品的平均评分
     * @param productId 商品ID
     * @return 平均评分
     */
    BigDecimal getProductAvgRating(Long productId);
    
    /**
     * 获取商品的评价数量
     * @param productId 商品ID
     * @return 评价数量
     */
    Integer getProductReviewCount(Long productId);
    
    /**
     * 获取商品的评价列表（分页）
     * @param page 分页参数
     * @param productId 商品ID
     * @return 评价列表
     */
    IPage<UserBehavior> getProductReviews(Page<UserBehavior> page, Long productId);
    
    /**
     * 获取某分类下的热门商品
     * @param categoryId 分类ID
     * @param limit 限制数量
     * @return 热门商品列表
     */
    List<Map<String, Object>> getHotProductsByCategory(Long categoryId, Integer limit);
    
    /**
     * 获取指定时间段内的用户行为统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 行为统计
     */
    Map<Integer, Map<String, Object>> getBehaviorStatsByDateRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计商品的收藏人数
     * @param productId 商品ID
     * @return 收藏人数
     */
    Integer getProductFavoriteCount(Long productId);
    
    /**
     * 分析用户兴趣偏好
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 用户兴趣偏好，包含categoryId、interestLevel
     */
    List<Map<String, Object>> analyzeUserInterests(Long userId, Integer limit);
    
    /**
     * 获取用户行为热力图数据
     * @param userId 用户ID
     * @param days 天数
     * @return 热力图数据，包含date、behaviorCount
     */
    List<Map<String, Object>> getUserBehaviorHeatmap(Long userId, Integer days);
    
    /**
     * 获取用户活跃度评分
     * @param userId 用户ID
     * @param days 天数
     * @return 活跃度评分（0-100）
     */
    Integer calculateUserActivityScore(Long userId, Integer days);
    
    /**
     * 清理指定日期之前的行为数据
     * @param beforeDate 日期
     * @return 清理的记录数
     */
    Integer cleanHistoricalData(LocalDateTime beforeDate);
    
    /**
     * 合并用户行为数据（用于用户合并场景）
     * @param sourceUserId 源用户ID
     * @param targetUserId 目标用户ID
     * @return 合并的记录数
     */
    Integer mergeUserBehaviors(Long sourceUserId, Long targetUserId);
} 