package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.ProductReview;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:04
 * @Entity model.entity.ProductReview
 */
public interface ProductReviewMapper extends BaseMapper<ProductReview> {

    /**
     * 根据商品ID查询评价
     * @param productId 商品ID
     * @return 评价列表
     */
    List<ProductReview> selectByProductId(@Param("productId") Long productId);

    /**
     * 分页查询商品评价
     * @param page 分页信息
     * @return 评价列表
     */
    IPage<ProductReview> selectPage(IPage<ProductReview> page);

    /**
     * 根据ID查询评价
     * @param id 评价ID
     * @return 评价信息
     */
    ProductReview selectById(@Param("id") Long id);

    /**
     * 插入新评价
     * @param productReview 评价信息
     * @return 插入结果
     */
    int insertProductReview(ProductReview productReview);

    /**
     * 更新评价信息
     * @param productReview 评价信息
     * @return 更新结果
     */
    int updateProductReview(ProductReview productReview);

    /**
     * 根据ID删除评价
     * @param id 评价ID
     * @return 删除结果
     */
    int deleteProductReview(@Param("id") Long id);
}




