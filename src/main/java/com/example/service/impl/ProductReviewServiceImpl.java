package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.ProductReviewMapper;
import com.example.model.dto.product.ProductReviewDTO;
import com.example.model.dto.product.ProductReviewPageDTO;
import com.example.model.entity.ProductReview;
import com.example.service.ProductReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Service实现
 * @createDate 2025-02-18 23:44:11
 */
@Service
@CacheConfig(cacheNames = "productReviewService")
public class ProductReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview>
        implements ProductReviewService {

    //private static final Logger logger = LoggerFactory.getLogger(ProductReviewServiceImpl.class);

    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #reviewDTO.productId")
    public boolean createReview(ProductReviewDTO reviewDTO) {
        // 校验是否重复评价
        if (baseMapper.checkReviewExists(reviewDTO.getUserId(), reviewDTO.getOrderId(), reviewDTO.getProductId()) > 0) {
            throw new BusinessException(ResultCode.REVIEW_ALREADY_EXISTS);
        }

        ProductReview review = new ProductReview();
        BeanUtils.copyProperties(reviewDTO, review);
        return save(review);
    }

    @Override
    @Cacheable(key = "'product:' + #queryDTO.productId + ':page:' + #queryDTO.hashCode()")
    public IPage<ProductReview> listReviewsPage(ProductReviewPageDTO queryDTO) {
        Page<ProductReview> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectReviewPage(page, queryDTO);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean updateReviewStatus(Long productId, Long reviewId, Integer status) {
        return baseMapper.updateStatus(reviewId, status) > 0;
    }

    @Override
    @Cacheable(key = "'product:' + #productId + ':stats'")
    public Map<String, Object> getProductRatingStats(Long productId) {
        return baseMapper.countProductRating(productId);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean updateReviewContent(Long productId, Long userId, Long reviewId, String content, String images) {
        return baseMapper.updateReviewContent(userId, reviewId, content, images) > 0;
    }

    @Override
    @Cacheable(key = "'user:' + #userId + ':reviews'")
    public List<ProductReview> getLatestUserReviews(Long userId, Integer limit) {
        return baseMapper.selectLatestUserReviews(userId, limit);
    }
}




