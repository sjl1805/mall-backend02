package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.product.ProductReviewDTO;
import com.example.model.dto.product.ProductReviewPageDTO;
import com.example.model.entity.ProductReview;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Service
 * @createDate 2025-02-18 23:44:11
 */
public interface ProductReviewService extends IService<ProductReview> {
    boolean createReview(ProductReviewDTO reviewDTO);

    IPage<ProductReview> listReviewsPage(ProductReviewPageDTO queryDTO);

    boolean updateReviewStatus(Long productId, Long reviewId, Integer status);

    Map<String, Object> getProductRatingStats(Long productId);

    boolean updateReviewContent(Long productId, Long userId, Long reviewId, String content, String images);

    List<ProductReview> getLatestUserReviews(Long userId, Integer limit);
}
