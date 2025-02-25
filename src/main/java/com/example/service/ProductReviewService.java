package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductReview;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:04
 */
public interface ProductReviewService extends IService<ProductReview> {

    /**
     * 根据商品ID查询评价
     *
     * @param productId 商品ID
     * @return 商品评价列表
     */
    List<ProductReview> selectByProductId(Long productId);

    /**
     * 分页查询商品评价
     *
     * @param page 分页信息
     * @return 商品评价列表
     */
    IPage<ProductReview> selectPage(IPage<ProductReview> page);

    /**
     * 根据ID查询商品评价
     *
     * @param id 商品评价ID
     * @return 商品评价信息
     */
    ProductReview selectById(Long id);

    /**
     * 新增商品评价
     *
     * @param productReview 商品评价信息
     * @return 插入结果
     */
    boolean insertProductReview(ProductReview productReview);

    /**
     * 更新商品评价信息
     *
     * @param productReview 商品评价信息
     * @return 更新结果
     */
    boolean updateProductReview(ProductReview productReview);

    /**
     * 根据ID删除商品评价
     *
     * @param id 商品评价ID
     * @return 删除结果
     */
    boolean deleteProductReview(Long id);

    /**
     * 计算商品平均评分
     *
     * @param productId 商品ID
     * @return 平均评分
     */
    Double calculateAverageRating(Long productId);

    /**
     * 统计各评分数量
     *
     * @param productId 商品ID
     * @return 各评分数量统计
     */
    List<Map<String, Object>> countByRating(Long productId);

    /**
     * 根据评分范围查询评价
     *
     * @param productId 商品ID
     * @param minRating 最低评分
     * @param maxRating 最高评分
     * @return 评价列表
     */
    List<ProductReview> selectByRatingRange(Long productId, Integer minRating, Integer maxRating);

    /**
     * 查询用户所有评价
     *
     * @param userId 用户ID
     * @return 用户评价列表
     */
    List<ProductReview> selectByUserId(Long userId);

    /**
     * 批量更新评价状态
     *
     * @param ids 评价ID列表
     * @param status 新状态
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);

    /**
     * 分页查询待审核评价
     *
     * @param page 分页参数
     * @return 待审核评价列表
     */
    IPage<ProductReview> selectPendingReviews(IPage<ProductReview> page);

    /**
     * 查询精选评价（高分评价）
     *
     * @param productId 商品ID
     * @param limit 限制数量
     * @return 精选评价列表
     */
    List<ProductReview> selectFeaturedReviews(Long productId, Integer limit);

    /**
     * 查询包含图片的评价
     *
     * @param productId 商品ID
     * @return 包含图片的评价列表
     */
    List<ProductReview> selectReviewsWithImages(Long productId);

    /**
     * 统计商品评价数据
     *
     * @param productId 商品ID
     * @return 评价统计数据（总数、平均分、好评率等）
     */
    Map<String, Object> getReviewStatistics(Long productId);

    /**
     * 回复商品评价
     *
     * @param reviewId 评价ID
     * @param replyContent 回复内容
     * @return 回复结果
     */
    boolean replyReview(Long reviewId, String replyContent);
}
