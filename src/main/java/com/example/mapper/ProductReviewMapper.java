package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.ProductReview;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【product_review(商品评价表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:04
 * @Entity model.entity.ProductReview
 */
public interface ProductReviewMapper extends BaseMapper<ProductReview> {

    /**
     * 分页查询商品评价
     * @param page 分页参数
     * @param productId 商品ID
     * @param query 查询条件
     * @return 分页结果（包含用户和商品信息）
     */
    Page<ProductReview> selectReviewPage(Page<ProductReview> page,
                                        @Param("productId") Long productId,
                                        @Param("query") ReviewQuery query);
    
    /**
     * 更新评价状态（审核）
     * @param reviewId 评价ID
     * @param productId 商品ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateReviewStatus(@Param("reviewId") Long reviewId,
                         @Param("productId") Long productId,
                         @Param("status") Integer status);
    
    /**
     * 获取评价统计信息
     * @param productId 商品ID
     * @return 包含各维度统计的Map
     */
    Map<String, Object> selectReviewSummary(@Param("productId") Long productId);
    
    /**
     * 查询最新评价
     * @param productId 商品ID
     * @param limit 数量限制
     * @return 最新评价列表（包含用户信息）
     */
    List<ProductReview> selectLatestReviews(@Param("productId") Long productId,
                                          @Param("limit") int limit);
    
    /**
     * 更新评价图片
     * @param reviewId 评价ID
     * @param userId 用户ID
     * @param images 图片JSON
     * @return 影响行数
     */
    int updateReviewImages(@Param("reviewId") Long reviewId,
                         @Param("userId") Long userId,
                         @Param("images") String images);

    @Data
    class ReviewQuery {
        private Integer rating;
    }
}




