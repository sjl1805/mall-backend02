package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.ProductReview;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:04
 */
public interface ProductReviewService extends IService<ProductReview> {

    /**
     * 根据商品ID查询评价
     * @param productId 商品ID
     * @return 商品评价列表
     */
    List<ProductReview> selectByProductId(Long productId);

    /**
     * 分页查询商品评价
     * @param page 分页信息
     * @return 商品评价列表
     */
    IPage<ProductReview> selectPage(IPage<ProductReview> page);

    /**
     * 根据ID查询商品评价
     * @param id 商品评价ID
     * @return 商品评价信息
     */
    ProductReview selectById(Long id);

    /**
     * 新增商品评价
     * @param productReview 商品评价信息
     * @return 插入结果
     */
    boolean insertProductReview(ProductReview productReview);

    /**
     * 更新商品评价信息
     * @param productReview 商品评价信息
     * @return 更新结果
     */
    boolean updateProductReview(ProductReview productReview);

    /**
     * 根据ID删除商品评价
     * @param id 商品评价ID
     * @return 删除结果
     */
    boolean deleteProductReview(Long id);
}
