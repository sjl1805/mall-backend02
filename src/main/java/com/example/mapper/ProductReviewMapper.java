package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.ProductReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:04
 * @Entity model.entity.ProductReview
 */
@Mapper
public interface ProductReviewMapper extends BaseMapper<ProductReview> {

    /**
     * 根据商品ID查询评价
     *
     * @param productId 商品ID
     * @return 评价列表
     */
    List<ProductReview> selectByProductId(@Param("productId") Long productId);

    /**
     * 分页查询商品评价
     *
     * @param page 分页信息
     * @return 评价列表
     */
    IPage<ProductReview> selectPage(IPage<ProductReview> page);

    /**
     * 根据ID查询评价
     *
     * @param id 评价ID
     * @return 评价信息
     */
    ProductReview selectById(@Param("id") Long id);

    /**
     * 插入新评价
     *
     * @param productReview 评价信息
     * @return 插入结果
     */
    int insertProductReview(ProductReview productReview);

    /**
     * 更新评价信息
     *
     * @param productReview 评价信息
     * @return 更新结果
     */
    int updateProductReview(ProductReview productReview);

    /**
     * 根据ID删除评价
     *
     * @param id 评价ID
     * @return 删除结果
     */
    int deleteProductReview(@Param("id") Long id);

    /**
     * 计算商品平均评分
     *
     * @param productId 商品ID
     * @return 平均评分
     */
    Double calculateAverageRating(@Param("productId") Long productId);

    /**
     * 统计各评分数量
     *
     * @param productId 商品ID
     * @return 各评分数量统计，格式：[{rating: 5, count: 10}, ...]
     */
    List<Map<String, Object>> countByRating(@Param("productId") Long productId);

    /**
     * 根据评分范围查询评价
     *
     * @param productId 商品ID
     * @param minRating 最低评分
     * @param maxRating 最高评分
     * @return 评价列表
     */
    List<ProductReview> selectByRatingRange(
            @Param("productId") Long productId,
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating);

    /**
     * 查询用户所有评价
     *
     * @param userId 用户ID
     * @return 用户评价列表
     */
    List<ProductReview> selectByUserId(@Param("userId") Long userId);

    /**
     * 批量更新评价状态
     *
     * @param ids 评价ID列表
     * @param status 新状态
     * @return 更新结果
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 查询评价详情（含用户和商品信息）
     *
     * @param id 评价ID
     * @return 评价详情
     */
    ProductReview selectReviewDetail(@Param("id") Long id);

    /**
     * 查询待审核评价数量
     *
     * @return 待审核评价数量
     */
    int countPendingReviews();

    /**
     * 更新评价有用度
     *
     * @param id 评价ID
     * @param increment 增量（1或-1）
     * @return 更新结果
     */
    int updateUsefulCount(@Param("id") Long id, @Param("increment") int increment);
}




