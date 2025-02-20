package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.ProductReviewDTO;
import com.example.model.dto.PageDTO;
import com.example.model.entity.ProductReview;
import com.example.service.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品评价管理控制器
 * 
 * @author 31815
 * @description 提供商品评价全生命周期管理功能，包含：
 *              1. 评价发布与审核
 *              2. 评价内容管理
 *              3. 评价数据分析
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/products/{productId}/reviews")
@RequiredArgsConstructor
@Tag(name = "ProductReview", description = "商品评价管理接口体系")
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    /**
     * 创建评价（带重复校验）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param reviewDTO 评价信息：
     *                 - userId: 用户ID（必须）
     *                 - rating: 评分（1-5分）
     *                 - content: 评价内容（最多500字）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - REVIEW_DUPLICATE(9001): 重复提交评价
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "评价创建", description = "用户提交商品评价接口")
    @ApiResponse(responseCode = "201", description = "评价创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复提交评价")
    public Result<Boolean> createReview(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "评价信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductReviewDTO.class))
            )
            @Valid @RequestBody ProductReviewDTO reviewDTO) {
        return Result.success(productReviewService.createReview(reviewDTO));
    }

    /**
     * 分页查询评价（管理端）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param queryDTO 分页参数：
     *                 - page: 当前页码（默认1）
     *                 - size: 每页数量（默认10，最大50）
     *                 - status: 状态过滤（0-隐藏，1-显示）
     * @return 分页结果包装对象
     */
    @GetMapping
    @Operation(summary = "评价分页查询", description = "管理端商品评价分页查询接口")
    @ApiResponse(responseCode = "200", description = "成功返回评价列表")
    public Result<IPage<ProductReviewDTO>> listReviews(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @Valid @Parameter(description = "分页查询参数") PageDTO<ProductReviewDTO> queryDTO) {
        return Result.success(productReviewService.listReviewsPage(queryDTO));
    }

    /**
     * 更新评价状态（事务操作）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param reviewId 评价ID（路径参数，必须大于0）
     * @param status 新状态（0-隐藏，1-显示）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - REVIEW_NOT_FOUND(9002): 评价不存在
     */
    @PatchMapping("/{reviewId}/status")
    @Operation(summary = "评价状态更新", description = "修改评价显示状态接口")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @ApiResponse(responseCode = "404", description = "评价不存在")
    public Result<Boolean> updateReviewStatus(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @Parameter(description = "评价ID", example = "1", required = true)
            @PathVariable @Min(1) Long reviewId,
            @Parameter(description = "新状态", schema = @Schema(allowableValues = {"0", "1"}), required = true)
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(productReviewService.updateReviewStatus(productId, reviewId, status));
    }

    /**
     * 获取评分统计（带缓存）
     * @param productId 商品ID（路径参数，必须大于0）
     * @return 统计指标：
     *         - averageRating: 平均评分
     *         - ratingDistribution: 各评分数量分布
     * @implNote 结果缓存优化，有效期2小时
     */
    @GetMapping("/stats")
    @Operation(summary = "评分统计", description = "查询商品的评分统计数据接口")
    @ApiResponse(responseCode = "200", description = "成功返回统计结果")
    public Result<Map<String, Object>> getRatingStats(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId) {
        return Result.success(productReviewService.getProductRatingStats(productId));
    }

    /**
     * 修改评价内容（带版本控制）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param userId 用户ID（必须大于0）
     * @param reviewId 评价ID（路径参数，必须大于0）
     * @param content 新评价内容（最多500字）
     * @param images 新图片地址（逗号分隔，最多5张）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - REVIEW_UPDATE_LIMIT(9003): 超过修改次数限制
     */
    @PutMapping("/{reviewId}")
    @Operation(summary = "评价内容更新", description = "用户修改自己的评价内容接口")
    @ApiResponse(responseCode = "200", description = "内容更新成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "403", description = "无修改权限")
    public Result<Boolean> updateReviewContent(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @Parameter(description = "用户ID", example = "1", required = true)
            @RequestParam @Min(1) Long userId,
            @Parameter(description = "评价ID", example = "1", required = true)
            @PathVariable @Min(1) Long reviewId,
            @Parameter(description = "新评价内容", required = true)
            @RequestParam String content,
            @Parameter(description = "新图片地址", required = true)
            @RequestParam String images) {
        return Result.success(productReviewService.updateReviewContent(productId, userId, reviewId, content, images));
    }

    /**
     * 获取用户最新评价（带缓存）
     * @param userId 用户ID（必须大于0）
     * @param limit 返回数量（默认5，最大20）
     * @return 最新评价列表（按时间倒序）
     * @implNote 结果缓存优化，有效期1小时
     */
    @GetMapping("/user-latest")
    @Operation(summary = "用户最新评价", description = "查询用户最近的商品评价接口")
    @ApiResponse(responseCode = "200", description = "成功返回评价列表")
    public Result<List<ProductReview>> getUserLatestReviews(
            @Parameter(description = "用户ID", example = "1", required = true)
            @RequestParam @Min(1) Long userId,
            @Parameter(description = "返回数量", example = "5", schema = @Schema(maximum = "20"))
            @RequestParam(defaultValue = "5") @Min(1) Integer limit) {
        return Result.success(productReviewService.getLatestUserReviews(userId, limit));
    }
} 