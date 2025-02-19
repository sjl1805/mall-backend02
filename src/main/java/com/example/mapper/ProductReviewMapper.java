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
 * 商品评价管理Mapper接口
 * 实现评价的增删改查、统计分析和状态管理
 * 
 * @author 毕业设计学生
 */
public interface ProductReviewMapper extends BaseMapper<ProductReview> {

    /**
     * 分页查询评价（支持多条件过滤）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含商品ID、用户ID等）
     * @return 分页结果（包含评价列表和分页信息）
     */
    IPage<ProductReview> selectReviewPage(IPage<ProductReview> page,
                                        @Param("query") ProductReviewPageDTO queryDTO);

    /**
     * 更新评价状态（管理员审核使用）
     * 
     * @param reviewId 评价ID（必填）
     * @param status   新状态（1-审核通过 2-已屏蔽）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE product_review SET status = #{status} WHERE id = #{reviewId}")
    int updateStatus(@Param("reviewId") Long reviewId,
                    @Param("status") Integer status);

    /**
     * 更新评价内容（用户修改评价时调用）
     * 
     * @param userId   用户ID（用于权限校验）
     * @param reviewId 评价ID（必填）
     * @param content  新评价内容（最多500字）
     * @param images   新图片URL（JSON数组格式）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE product_review SET content = #{content}, images = #{images} " +
            "WHERE id = #{reviewId} AND user_id = #{userId}")
    int updateReviewContent(@Param("userId") Long userId,
                           @Param("reviewId") Long reviewId,
                           @Param("content") String content,
                           @Param("images") String images);

    /**
     * 统计商品评分（用于商品详情页展示）
     * 
     * @param productId 商品ID（必填）
     * @return 包含平均分和各星级数量的统计结果
     */
    Map<String, Object> countProductRating(@Param("productId") Long productId);

    /**
     * 获取用户最新评价（用于个人中心展示）
     * 
     * @param userId 用户ID（必填）
     * @param limit  最大返回数量（1-20）
     * @return 按时间倒序排列的评价列表
     */
    List<ProductReview> selectLatestUserReviews(@Param("userId") Long userId,
                                                 @Param("limit") Integer limit);

    /**
     * 检查评价是否存在（防止重复评价）
     * 
     * @param userId    用户ID（必填）
     * @param orderId   订单ID（必填）
     * @param productId 商品ID（必填）
     * @return 存在返回1，否则返回0
     */
    int checkReviewExists(@Param("userId") Long userId,
                         @Param("orderId") Long orderId,
                         @Param("productId") Long productId);
}




