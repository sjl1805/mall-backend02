package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.ProductReviewDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.ProductReview;

import java.util.List;
import java.util.Map;

/**
 * 商品评价服务接口
 * 
 * @author 31815
 * @description 提供商品评价管理功能，包含：
 *              1. 评价的创建与修改
 *              2. 评价状态管理
 *              3. 评价统计与查询
 * @createDate 2025-02-18 23:44:11
 */
public interface ProductReviewService extends IService<ProductReview> {

    /**
     * 创建评价（带校验）
     * @param reviewDTO 评价信息，包含：
     *                  - userId: 用户ID（必须）
     *                  - productId: 商品ID（必须）
     *                  - orderId: 订单ID（必须）
     *                  - rating: 评分（1-5）
     *                  - content: 评价内容
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当重复评价时抛出REVIEW_ALREADY_EXISTS
     */
    boolean createReview(ProductReviewDTO reviewDTO);

    /**
     * 分页查询评价（带缓存）
     * @param queryDTO 分页参数：
     *                 - productId: 商品ID（必须）
     *                 - page: 当前页
     *                 - size: 每页数量
     *                 - rating: 评分过滤
     * @return 分页结果（包含评价详情）
     * @implNote 结果缓存优化，有效期15分钟
     */
    IPage<ProductReviewDTO> listReviewsPage(PageDTO<ProductReviewDTO> queryDTO);

    /**
     * 更新评价状态（管理端）
     * @param productId 商品ID
     * @param reviewId 评价ID
     * @param status 新状态（0-待审核，1-已发布，2-已隐藏）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当状态非法时抛出
     */
    boolean updateReviewStatus(Long productId, Long reviewId, Integer status);

    /**
     * 获取商品评分统计
     * @param productId 商品ID
     * @return 统计结果（包含平均分、各评分数量）
     * @implNote 结果缓存优化，有效期1小时
     */
    Map<String, Object> getProductRatingStats(Long productId);

    /**
     * 修改评价内容（用户端）
     * @param productId 商品ID
     * @param userId 用户ID
     * @param reviewId 评价ID
     * @param content 新内容
     * @param images 新图片
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当权限不足时抛出
     */
    boolean updateReviewContent(Long productId, Long userId, Long reviewId, String content, String images);

    /**
     * 获取用户最新评价
     * @param userId 用户ID
     * @param limit 最大数量
     * @return 评价列表（按时间倒序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    List<ProductReview> getLatestUserReviews(Long userId, Integer limit);
}
