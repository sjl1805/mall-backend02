package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ProductReviewMapper;
import com.example.model.entity.ProductReview;
import com.example.service.ProductReviewService;
import org.springframework.stereotype.Service;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:04
 */
@Service
public class ProductReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview>
        implements ProductReviewService {

}




