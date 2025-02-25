package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common. Result;
import com.example.common.ResultCode;
import com.example.model.entity.ProductReview;
import com.example.service.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品评价控制器
 * <p>
 * 提供商品评价相关的API接口，包括评价的查询、添加、修改、删除等操作
 * 以及评价统计、筛选等高级功能
 */
@Tag(name = "ProductReview", description = "商品评价管理API")
@RestController
@RequestMapping("/productReview")
@Validated
@Slf4j
public class ProductReviewController {

    @Autowired
    private ProductReviewService productReviewService;

    @Operation(summary = "根据商品ID查询评价", description = "获取指定商品的所有评价")
    @GetMapping("/product/{productId}")
    public  Result<List<ProductReview>> getReviewsByProductId(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId) {
        log.info("根据商品ID查询评价请求: productId={}", productId);
        List<ProductReview> reviews = productReviewService.selectByProductId(productId);
        log.info("根据商品ID查询评价成功: productId={}, count={}", productId, reviews.size());
        return  Result.success(reviews);
    }

    @Operation(summary = "分页查询商品评价", description = "管理员分页查询所有商品评价")
    @GetMapping("/list")
    public  Result<IPage<ProductReview>> getReviewList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询商品评价请求: page={}, size={}", page, size);
        IPage<ProductReview> pageParam = new Page<>(page, size);
        IPage<ProductReview> result = productReviewService.selectPage(pageParam);
        log.info("分页查询商品评价成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }

    @Operation(summary = "根据ID查询商品评价", description = "获取特定评价的详细信息")
    @GetMapping("/{id}")
    public  Result<ProductReview> getReviewById(
            @Parameter(description = "评价ID", required = true) @PathVariable Long id) {
        log.info("根据ID查询商品评价请求: id={}", id);
        ProductReview review = productReviewService.selectById(id);
        if (review != null) {
            log.info("根据ID查询商品评价成功: id={}", id);
            return  Result.success(review);
        } else {
            log.warn("根据ID查询商品评价失败: id={}, 评价不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "评价不存在");
        }
    }

    @Operation(summary = "新增商品评价", description = "用户添加商品评价")
    @PostMapping("/add")
    public  Result<Boolean> addProductReview(@Valid @RequestBody ProductReview productReview) {
        log.info("新增商品评价请求: userId={}, productId={}, rating={}", 
                productReview.getUserId(), productReview.getProductId(), productReview.getRating());
        
        // 验证评分范围
        if (productReview.getRating() == null || productReview.getRating() < 1 || productReview.getRating() > 5) {
            log.warn("新增商品评价失败: 评分无效, rating={}", productReview.getRating());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "评分必须在1-5之间");
        }
        
        // 验证用户只能添加自己的评价
        if (!isCurrentUserOrAdmin(productReview.getUserId())) {
            log.warn("新增商品评价失败: 权限不足, userId={}", productReview.getUserId());
            return  Result.failed(ResultCode.FORBIDDEN, "无权为其他用户添加评价");
        }
        
        boolean result = productReviewService.insertProductReview(productReview);
        if (result) {
            log.info("新增商品评价成功: userId={}, productId={}, id={}", 
                    productReview.getUserId(), productReview.getProductId(), productReview.getId());
            return  Result.success(true);
        } else {
            log.warn("新增商品评价失败: userId={}, productId={}", 
                    productReview.getUserId(), productReview.getProductId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新商品评价", description = "用户修改自己的评价或管理员修改任意评价")
    @PutMapping("/update")
    public  Result<Boolean> updateProductReview(@Valid @RequestBody ProductReview productReview) {
        log.info("更新商品评价请求: id={}", productReview.getId());
        
        // 参数验证
        if (productReview.getId() == null) {
            log.warn("更新商品评价失败: ID不能为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "评价ID不能为空");
        }
        
        // 验证评分范围
        if (productReview.getRating() != null && (productReview.getRating() < 1 || productReview.getRating() > 5)) {
            log.warn("更新商品评价失败: 评分无效, rating={}", productReview.getRating());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "评分必须在1-5之间");
        }
        
        // 获取原评价信息，验证权限
        ProductReview existingReview = productReviewService.selectById(productReview.getId());
        if (existingReview == null) {
            log.warn("更新商品评价失败: id={}, 评价不存在", productReview.getId());
            return  Result.failed(ResultCode.NOT_FOUND, "评价不存在");
        }
        
        // 验证用户只能修改自己的评价
        if (!isCurrentUserOrAdmin(existingReview.getUserId())) {
            log.warn("更新商品评价失败: 权限不足, userId={}", existingReview.getUserId());
            return  Result.failed(ResultCode.FORBIDDEN, "无权修改他人的评价");
        }
        
        boolean result = productReviewService.updateProductReview(productReview);
        if (result) {
            log.info("更新商品评价成功: id={}", productReview.getId());
            return  Result.success(true);
        } else {
            log.warn("更新商品评价失败: id={}", productReview.getId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除商品评价", description = "用户删除自己的评价或管理员删除任意评价")
    @DeleteMapping("/{id}")
    public  Result<Boolean> deleteProductReview(
            @Parameter(description = "评价ID", required = true) @PathVariable Long id) {
        log.info("删除商品评价请求: id={}", id);
        
        // 获取评价信息，验证权限
        ProductReview review = productReviewService.selectById(id);
        if (review == null) {
            log.warn("删除商品评价失败: id={}, 评价不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "评价不存在");
        }
        
        // 验证用户只能删除自己的评价
        if (!isCurrentUserOrAdmin(review.getUserId())) {
            log.warn("删除商品评价失败: 权限不足, userId={}", review.getUserId());
            return  Result.failed(ResultCode.FORBIDDEN, "无权删除他人的评价");
        }
        
        boolean result = productReviewService.deleteProductReview(id);
        if (result) {
            log.info("删除商品评价成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除商品评价失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "计算商品平均评分", description = "获取指定商品的平均评分")
    @GetMapping("/average/{productId}")
    public  Result<Double> calculateAverageRating(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId) {
        log.info("计算商品平均评分请求: productId={}", productId);
        Double averageRating = productReviewService.calculateAverageRating(productId);
        log.info("计算商品平均评分成功: productId={}, averageRating={}", productId, averageRating);
        return  Result.success(averageRating);
    }
    
    @Operation(summary = "统计各评分数量", description = "获取指定商品各评分的数量分布")
    @GetMapping("/count/{productId}")
    public  Result<List<Map<String, Object>>> countByRating(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId) {
        log.info("统计各评分数量请求: productId={}", productId);
        List<Map<String, Object>> ratingCounts = productReviewService.countByRating(productId);
        log.info("统计各评分数量成功: productId={}, counts={}", productId, ratingCounts);
        return  Result.success(ratingCounts);
    }
    
    @Operation(summary = "根据评分范围查询评价", description = "获取指定评分范围内的商品评价")
    @GetMapping("/range/{productId}")
    public  Result<List<ProductReview>> selectByRatingRange(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId,
            @Parameter(description = "最低评分") @RequestParam(defaultValue = "1") Integer minRating,
            @Parameter(description = "最高评分") @RequestParam(defaultValue = "5") Integer maxRating) {
        log.info("根据评分范围查询评价请求: productId={}, minRating={}, maxRating={}", productId, minRating, maxRating);
        
        // 验证评分范围
        if (minRating < 1 || minRating > 5 || maxRating < 1 || maxRating > 5 || minRating > maxRating) {
            log.warn("根据评分范围查询评价失败: 评分范围无效, minRating={}, maxRating={}", minRating, maxRating);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "评分范围必须在1-5之间，且最低分不能高于最高分");
        }
        
        List<ProductReview> reviews = productReviewService.selectByRatingRange(productId, minRating, maxRating);
        log.info("根据评分范围查询评价成功: productId={}, minRating={}, maxRating={}, count={}", 
                productId, minRating, maxRating, reviews.size());
        return  Result.success(reviews);
    }
    
    @Operation(summary = "查询用户所有评价", description = "获取指定用户发表的所有评价")
    @GetMapping("/user/{userId}")
    public  Result<List<ProductReview>> selectByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("查询用户所有评价请求: userId={}", userId);
        List<ProductReview> reviews = productReviewService.selectByUserId(userId);
        log.info("查询用户所有评价成功: userId={}, count={}", userId, reviews.size());
        return  Result.success(reviews);
    }
    
    @Operation(summary = "批量更新评价状态", description = "管理员批量更新评价状态")
    @PutMapping("/batch/status/{status}")
    public  Result<Boolean> batchUpdateStatus(
            @RequestBody List<Long> ids,
            @Parameter(description = "新状态: 0-待审核 1-通过 2-拒绝", required = true) @PathVariable Integer status) {
        log.info("批量更新评价状态请求: ids={}, status={}", ids, status);
        
        // 验证参数有效性
        if (ids == null || ids.isEmpty()) {
            log.warn("批量更新评价状态失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "评价ID列表不能为空");
        }
        
        if (status < 0 || status > 2) {
            log.warn("批量更新评价状态失败: 状态值无效, status={}", status);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "状态值必须在0-2之间");
        }
        
        boolean result = productReviewService.batchUpdateStatus(ids, status);
        if (result) {
            log.info("批量更新评价状态成功: ids={}, status={}", ids, status);
            return  Result.success(true);
        } else {
            log.warn("批量更新评价状态失败: ids={}, status={}", ids, status);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "分页查询待审核评价", description = "管理员分页查询待审核的评价")
    @GetMapping("/pending")
    public  Result<IPage<ProductReview>> selectPendingReviews(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询待审核评价请求: page={}, size={}", page, size);
        IPage<ProductReview> pageParam = new Page<>(page, size);
        IPage<ProductReview> result = productReviewService.selectPendingReviews(pageParam);
        log.info("分页查询待审核评价成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }
    
    @Operation(summary = "查询精选评价", description = "获取指定商品的高分精选评价")
    @GetMapping("/featured/{productId}")
    public  Result<List<ProductReview>> selectFeaturedReviews(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "3") Integer limit) {
        log.info("查询精选评价请求: productId={}, limit={}", productId, limit);
        List<ProductReview> reviews = productReviewService.selectFeaturedReviews(productId, limit);
        log.info("查询精选评价成功: productId={}, limit={}, count={}", productId, limit, reviews.size());
        return  Result.success(reviews);
    }
    
    @Operation(summary = "查询包含图片的评价", description = "获取指定商品包含图片的评价")
    @GetMapping("/images/{productId}")
    public  Result<List<ProductReview>> selectReviewsWithImages(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId) {
        log.info("查询包含图片的评价请求: productId={}", productId);
        List<ProductReview> reviews = productReviewService.selectReviewsWithImages(productId);
        log.info("查询包含图片的评价成功: productId={}, count={}", productId, reviews.size());
        return  Result.success(reviews);
    }
    
    @Operation(summary = "统计商品评价数据", description = "获取指定商品的评价统计数据")
    @GetMapping("/statistics/{productId}")
    public  Result<Map<String, Object>> getReviewStatistics(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId) {
        log.info("统计商品评价数据请求: productId={}", productId);
        Map<String, Object> statistics = productReviewService.getReviewStatistics(productId);
        log.info("统计商品评价数据成功: productId={}, statistics={}", productId, statistics);
        return  Result.success(statistics);
    }
    
    @Operation(summary = "回复商品评价", description = "管理员回复用户评价")
    @PostMapping("/{reviewId}/reply")
    public  Result<Boolean> replyReview(
            @Parameter(description = "评价ID", required = true) @PathVariable Long reviewId,
            @RequestBody Map<String, String> replyMap) {
        String replyContent = replyMap.get("replyContent");
        log.info("回复商品评价请求: reviewId={}", reviewId);
        
        // 参数验证
        if (replyContent == null || replyContent.trim().isEmpty()) {
            log.warn("回复商品评价失败: 回复内容为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "回复内容不能为空");
        }
        
        // 检查评价是否存在
        ProductReview review = productReviewService.selectById(reviewId);
        if (review == null) {
            log.warn("回复商品评价失败: reviewId={}, 评价不存在", reviewId);
            return  Result.failed(ResultCode.NOT_FOUND, "评价不存在");
        }
        
        boolean result = productReviewService.replyReview(reviewId, replyContent);
        if (result) {
            log.info("回复商品评价成功: reviewId={}", reviewId);
            return  Result.success(true);
        } else {
            log.warn("回复商品评价失败: reviewId={}", reviewId);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    /**
     * 辅助方法：判断当前用户是否为管理员或操作用户本人
     */
    private boolean isCurrentUserOrAdmin(Long userId) {
        // 此处需要根据您的安全框架实现具体逻辑
        // 简化处理，实际应从SecurityContext中获取用户信息进行判断
        return true; // 默认允许，实际实现中应该返回正确的判断结果
    }
} 

