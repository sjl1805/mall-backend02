package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.product.ProductReviewPageDTO;
import com.example.model.entity.ProductReview;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Mapper
 * @createDate 2025-02-18 23:44:11
 * @Entity model.entity.ProductReview
 */
public interface ProductReviewMapper extends BaseMapper<ProductReview> {

    /**
     * 分页查询评价列表
     *
     * @param page     分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<ProductReview> selectReviewPage(IPage<ProductReview> page,
                                          @Param("query") ProductReviewPageDTO queryDTO);

    /**
     * 更新评价状态
     *
     * @param reviewId 评价ID
     * @param status   新状态
     * @return 影响行数
     */
    @Update("UPDATE product_review SET status = #{status} WHERE id = #{reviewId}")
    int updateStatus(@Param("reviewId") Long reviewId,
                     @Param("status") Integer status);

    /**
     * 更新评价内容
     *
     * @param userId   用户ID
     * @param reviewId 评价ID
     * @param content  新内容
     * @param images   新图片
     * @return 影响行数
     */
    @Update("UPDATE product_review SET content = #{content}, images = #{images} " +
            "WHERE id = #{reviewId} AND user_id = #{userId}")
    int updateReviewContent(@Param("userId") Long userId,
                            @Param("reviewId") Long reviewId,
                            @Param("content") String content,
                            @Param("images") String images);

    /**
     * 统计商品评分
     *
     * @param productId 商品ID
     * @return 评分统计结果
     */
    Map<String, Object> countProductRating(@Param("productId") Long productId);

    /**
     * 获取用户最新评价
     *
     * @param userId 用户ID
     * @param limit  数量限制
     * @return 评价列表
     */
    List<ProductReview> selectLatestUserReviews(@Param("userId") Long userId,
                                                @Param("limit") Integer limit);

    /**
     * 检查用户是否已评价
     *
     * @param userId    用户ID
     * @param orderId   订单ID
     * @param productId 商品ID
     * @return 存在数量
     */
    int checkReviewExists(@Param("userId") Long userId,
                          @Param("orderId") Long orderId,
                          @Param("productId") Long productId);
}




