package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductReviewMapper;
import com.example.model.entity.ProductReview;
import com.example.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品评价服务实现类
 * <p>
 * 该类实现了商品评价相关的业务逻辑，包括评价的添加、修改、删除和查询等功能。
 * 商品评价是电商系统中重要的用户反馈机制，对提高用户信任感和购买决策有重要影响。
 * 评价数据同时也是商品质量监控和销售分析的重要数据来源。
 * 使用了Spring缓存机制对评价信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:04
 */
@Service
@CacheConfig(cacheNames = "productReviews") // 指定该服务类的缓存名称为"productReviews"
public class ProductReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview>
        implements ProductReviewService {

    @Autowired
    private ProductReviewMapper productReviewMapper;

    /**
     * 根据商品ID查询评价列表
     * <p>
     * 该方法从缓存或数据库获取指定商品的所有评价信息，
     * 用于商品详情页展示用户评价，帮助潜在买家做出购买决策
     *
     * @param productId 商品ID
     * @return 商品评价列表
     */
    @Override
    @Cacheable(value = "productReviews", key = "#productId") // 缓存商品评价信息，提高查询效率
    public List<ProductReview> selectByProductId(Long productId) {
        return productReviewMapper.selectByProductId(productId);
    }

    /**
     * 分页查询商品评价数据
     * <p>
     * 该方法用于前台分页展示评价和后台管理系统审核评价，
     * 支持按评分、时间等条件排序
     *
     * @param page 分页参数
     * @return 商品评价分页数据
     */
    @Override
    public IPage<ProductReview> selectPage(IPage<ProductReview> page) {
        return productReviewMapper.selectPage(page);
    }

    /**
     * 根据ID查询商品评价
     *
     * @param id 评价ID
     * @return 商品评价实体
     */
    @Override
    @Cacheable(value = "productReviews", key = "#id") // 缓存评价详情，提高查询效率
    public ProductReview selectById(Long id) {
        return productReviewMapper.selectById(id);
    }

    /**
     * 添加商品评价
     * <p>
     * 该方法用于用户在订单完成后提交商品评价，
     * 包括评分、评价内容、图片等信息，
     * 添加后可能需要触发商品评分的重新计算
     *
     * @param productReview 商品评价实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productReviews", key = "#productReview.id") // 清除评价缓存
    public boolean insertProductReview(ProductReview productReview) {
        return productReviewMapper.insert(productReview) > 0;
    }

    /**
     * 更新商品评价
     * <p>
     * 该方法用于用户修改评价或后台管理系统审核评价，
     * 如修改评价内容、状态（显示/隐藏）等，
     * 并清除相关缓存，确保数据一致性
     *
     * @param productReview 商品评价实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productReviews", key = "#productReview.id") // 清除评价缓存
    public boolean updateProductReview(ProductReview productReview) {
        return productReviewMapper.updateById(productReview) > 0;
    }

    /**
     * 删除商品评价
     * <p>
     * 该方法用于用户删除自己的评价或后台管理系统删除违规评价，
     * 删除后可能需要触发商品评分的重新计算，
     * 并清除相关缓存，确保数据一致性
     *
     * @param id 评价ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "productReviews", key = "#id") // 清除被删除评价的缓存
    public boolean deleteProductReview(Long id) {
        return productReviewMapper.deleteById(id) > 0;
    }

    /**
     * 计算商品平均评分
     * <p>
     * 该方法计算指定商品的平均评分，
     * 是商品评分展示和排序的基础
     *
     * @param productId 商品ID
     * @return 平均评分
     */
    @Override
    @Cacheable(key = "'avg_' + #productId")
    public Double calculateAverageRating(Long productId) {
        return productReviewMapper.calculateAverageRating(productId);
    }

    /**
     * 统计各评分数量
     * <p>
     * 该方法统计指定商品各评分的数量分布，
     * 用于前台展示评分分布图表
     *
     * @param productId 商品ID
     * @return 各评分数量统计
     */
    @Override
    @Cacheable(key = "'count_' + #productId")
    public List<Map<String, Object>> countByRating(Long productId) {
        return productReviewMapper.countByRating(productId);
    }

    /**
     * 根据评分范围查询评价
     * <p>
     * 该方法查询指定评分范围内的商品评价，
     * 用于用户筛选查看特定评分的评价
     *
     * @param productId 商品ID
     * @param minRating 最低评分
     * @param maxRating 最高评分
     * @return 评价列表
     */
    @Override
    @Cacheable(key = "'range_' + #productId + '_' + #minRating + '_' + #maxRating")
    public List<ProductReview> selectByRatingRange(Long productId, Integer minRating, Integer maxRating) {
        return productReviewMapper.selectByRatingRange(productId, minRating, maxRating);
    }

    /**
     * 查询用户所有评价
     * <p>
     * 该方法查询指定用户发表的所有评价，
     * 用于用户中心展示个人评价记录
     *
     * @param userId 用户ID
     * @return 用户评价列表
     */
    @Override
    @Cacheable(key = "'user_' + #userId")
    public List<ProductReview> selectByUserId(Long userId) {
        return productReviewMapper.selectByUserId(userId);
    }

    /**
     * 批量更新评价状态
     * <p>
     * 该方法一次性更新多个评价的状态，
     * 用于后台管理系统批量审核评价
     *
     * @param ids    评价ID列表
     * @param status 新状态
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        return productReviewMapper.batchUpdateStatus(ids, status) > 0;
    }

    /**
     * 分页查询待审核评价
     * <p>
     * 该方法查询状态为待审核的评价，
     * 用于后台管理系统的评价审核功能
     *
     * @param page 分页参数
     * @return 待审核评价列表
     */
    @Override
    public IPage<ProductReview> selectPendingReviews(IPage<ProductReview> page) {
        QueryWrapper<ProductReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0); // 假设0表示待审核
        return page(page, queryWrapper);
    }

    /**
     * 查询精选评价（高分评价）
     * <p>
     * 该方法查询指定商品的高分评价，
     * 用于前台展示精选评价，提高用户购买意愿
     *
     * @param productId 商品ID
     * @param limit     限制数量
     * @return 精选评价列表
     */
    @Override
    @Cacheable(key = "'featured_' + #productId + '_' + #limit")
    public List<ProductReview> selectFeaturedReviews(Long productId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 3; // 默认返回3条
        }

        QueryWrapper<ProductReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId)
                .eq("status", 1) // 已审核通过
                .ge("rating", 4) // 4分及以上
                .orderByDesc("rating", "create_time")
                .last("LIMIT " + limit);

        return list(queryWrapper);
    }

    /**
     * 查询包含图片的评价
     * <p>
     * 该方法查询包含图片的商品评价，
     * 用于前台展示图文评价，提供更直观的产品使用效果
     *
     * @param productId 商品ID
     * @return 包含图片的评价列表
     */
    @Override
    @Cacheable(key = "'images_' + #productId")
    public List<ProductReview> selectReviewsWithImages(Long productId) {
        QueryWrapper<ProductReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId)
                .eq("status", 1)
                .isNotNull("images")
                .ne("images", "[]") // 非空图片数组
                .ne("images", "") // 非空字符串
                .orderByDesc("create_time");

        return list(queryWrapper);
    }

    /**
     * 统计商品评价数据
     * <p>
     * 该方法汇总指定商品的评价统计数据，
     * 包括总评价数、平均分、各分数占比等，
     * 用于前台展示评价概览
     *
     * @param productId 商品ID
     * @return 评价统计数据
     */
    @Override
    @Cacheable(key = "'stats_' + #productId")
    public Map<String, Object> getReviewStatistics(Long productId) {
        Map<String, Object> result = new HashMap<>();

        // 获取平均分
        Double avgRating = calculateAverageRating(productId);
        result.put("averageRating", avgRating != null ? avgRating : 0);

        // 获取各评分数量
        List<Map<String, Object>> ratingCounts = countByRating(productId);
        result.put("ratingDistribution", ratingCounts);

        // 计算总评价数
        int totalCount = 0;
        for (Map<String, Object> rating : ratingCounts) {
            totalCount += ((Number) rating.get("count")).intValue();
        }
        result.put("totalCount", totalCount);

        // 计算好评率（4-5分）
        long goodCount = ratingCounts.stream()
                .filter(m -> ((Number) m.get("rating")).intValue() >= 4)
                .mapToLong(m -> ((Number) m.get("count")).longValue())
                .sum();

        double goodRate = totalCount > 0 ? (double) goodCount / totalCount * 100 : 0;
        result.put("goodRate", String.format("%.1f%%", goodRate));

        return result;
    }

    /**
     * 回复商品评价
     * <p>
     * 该方法用于商家或管理员回复用户评价，
     * 提高商家与用户的互动性，解决用户问题
     *
     * @param reviewId     评价ID
     * @param replyContent 回复内容
     * @return 回复结果
     */
    @Override
    @Transactional
    @CacheEvict(key = "#reviewId")
    public boolean replyReview(Long reviewId, String replyContent) {
        // 这里需要一个评价回复表，或者在评价表中添加回复字段
        // 简化实现，实际中应该有专门的回复表
        // return reviewReplyMapper.insertReply(reviewId, replyContent);
        return true; // 暂时返回true，需要实际实现
    }
}




