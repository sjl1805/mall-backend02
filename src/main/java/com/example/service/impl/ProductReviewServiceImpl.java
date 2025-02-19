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
 * 商品评价服务实现类
 * 
 * @author 31815
 * @description 实现商品评价核心业务逻辑，包含：
 *              1. 评价校验与状态管理
 *              2. 统计计算与缓存优化
 *              3. 事务控制与权限校验
 * @createDate 2025-02-18 23:44:11
 */
@Service
@CacheConfig(cacheNames = "productReviewService")
public class ProductReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview>
        implements ProductReviewService {

    /**
     * 创建评价（事务管理）
     * @param reviewDTO 评价信息
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 校验是否重复评价
     *           2. 初始化评价状态为待审核（0）
     *           3. 清除商品评价缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #reviewDTO.productId")
    public boolean createReview(ProductReviewDTO reviewDTO) {
        if (baseMapper.checkReviewExists(reviewDTO.getUserId(), reviewDTO.getOrderId(), reviewDTO.getProductId()) > 0) {
            throw new BusinessException(ResultCode.REVIEW_ALREADY_EXISTS);
        }

        ProductReview review = new ProductReview();
        BeanUtils.copyProperties(reviewDTO, review);
        review.setStatus(0); // 初始状态为待审核
        return save(review);
    }

    /**
     * 分页查询评价（缓存优化）
     * @param queryDTO 分页参数
     * @return 分页结果
     * @implNote 缓存策略：
     *           1. 缓存键：product:{productId}:page:{queryDTO.hashCode}
     *           2. 缓存时间：15分钟
     */
    @Override
    @Cacheable(key = "'product:' + #queryDTO.productId + ':page:' + #queryDTO.hashCode()")
    public IPage<ProductReview> listReviewsPage(ProductReviewPageDTO queryDTO) {
        Page<ProductReview> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectReviewPage(page, queryDTO);
    }

    /**
     * 更新评价状态（管理端）
     * @param productId 商品ID
     * @param reviewId 评价ID
     * @param status 新状态
     * @return 操作结果
     * @implNote 状态变更后清除商品统计缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean updateReviewStatus(Long productId, Long reviewId, Integer status) {
        return baseMapper.updateStatus(reviewId, status) > 0;
    }

    /**
     * 获取商品评分统计（缓存优化）
     * @param productId 商品ID
     * @return 统计结果
     * @implNote 缓存策略：
     *           1. 缓存键：product:{productId}:stats
     *           2. 缓存时间：1小时
     */
    @Override
    @Cacheable(key = "'product:' + #productId + ':stats'")
    public Map<String, Object> getProductRatingStats(Long productId) {
        return baseMapper.countProductRating(productId);
    }

    /**
     * 修改评价内容（权限校验）
     * @param productId 商品ID
     * @param userId 用户ID
     * @param reviewId 评价ID
     * @param content 新内容
     * @param images 新图片
     * @return 操作结果
     * @implNote 执行逻辑：
     *           1. 验证用户对评价的所有权
     *           2. 更新内容并清除缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'product:' + #productId")
    public boolean updateReviewContent(Long productId, Long userId, Long reviewId, String content, String images) {
        return baseMapper.updateReviewContent(userId, reviewId, content, images) > 0;
    }

    /**
     * 获取用户最新评价（缓存优化）
     * @param userId 用户ID
     * @param limit 最大数量
     * @return 评价列表
     * @implNote 缓存策略：
     *           1. 缓存键：user:{userId}:reviews
     *           2. 缓存时间：30分钟
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':reviews'")
    public List<ProductReview> getLatestUserReviews(Long userId, Integer limit) {
        return baseMapper.selectLatestUserReviews(userId, limit);
    }
}




