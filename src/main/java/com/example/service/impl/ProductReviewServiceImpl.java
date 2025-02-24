package com.example.service.impl;

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

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:04
 */
@Service
@CacheConfig(cacheNames = "productReviews")
public class ProductReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview>
        implements ProductReviewService {

    @Autowired
    private ProductReviewMapper productReviewMapper;

    @Override
    @Cacheable(value = "productReviews", key = "#productId")
    public List<ProductReview> selectByProductId(Long productId) {
        return productReviewMapper.selectByProductId(productId);
    }

    @Override
    public IPage<ProductReview> selectPage(IPage<ProductReview> page) {
        return productReviewMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "productReviews", key = "#id")
    public ProductReview selectById(Long id) {
        return productReviewMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "productReviews", key = "#productReview.id")
    public boolean insertProductReview(ProductReview productReview) {
        return productReviewMapper.insert(productReview) > 0;
    }

    @Override
    @CacheEvict(value = "productReviews", key = "#productReview.id")
    public boolean updateProductReview(ProductReview productReview) {
        return productReviewMapper.updateById(productReview) > 0;
    }

    @Override
    @CacheEvict(value = "productReviews", key = "#id")
    public boolean deleteProductReview(Long id) {
        return productReviewMapper.deleteById(id) > 0;
    }
}




