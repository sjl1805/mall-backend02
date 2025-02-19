package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.product.ProductReviewDTO;
import com.example.model.dto.product.ProductReviewPageDTO;
import com.example.model.entity.ProductReview;
import com.example.service.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products/{productId}/reviews")
@RequiredArgsConstructor
@Tag(name = "ProductReview", description = "商品评价管理接口")
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建评价", description = "用户提交商品评价")
    @ApiResponse(responseCode = "201", description = "评价创建成功")
    public Result<Boolean> createReview(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "评价信息",
                required = true,
                content = @Content(schema = @Schema(implementation = ProductReviewDTO.class))
            )
            @Valid @RequestBody ProductReviewDTO reviewDTO) {
        return Result.success(productReviewService.createReview(reviewDTO));
    }

    @GetMapping
    @Operation(summary = "分页查询评价", description = "分页获取商品评价列表")
    @ApiResponse(responseCode = "200", description = "成功返回评价列表")
    public Result<IPage<ProductReview>> listReviews(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @Valid ProductReviewPageDTO queryDTO) {
        return Result.success(productReviewService.listReviewsPage(queryDTO));
    }

    @PatchMapping("/{reviewId}/status")
    @Operation(summary = "更新评价状态", description = "修改评价显示状态")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    public Result<Boolean> updateReviewStatus(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @Parameter(description = "评价ID", example = "1") @PathVariable @Min(1) Long reviewId,
            @Parameter(description = "新状态：0-隐藏 1-显示", example = "1") 
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(productReviewService.updateReviewStatus(productId, reviewId, status));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取评分统计", description = "查询商品的评分统计数据")
    @ApiResponse(responseCode = "200", description = "成功返回统计结果")
    public Result<Map<String, Object>> getRatingStats(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId) {
        return Result.success(productReviewService.getProductRatingStats(productId));
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "修改评价内容", description = "用户修改自己的评价内容")
    @ApiResponse(responseCode = "200", description = "内容更新成功")
    public Result<Boolean> updateReviewContent(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @Parameter(description = "用户ID", example = "1") @RequestParam @Min(1) Long userId,
            @Parameter(description = "评价ID", example = "1") @PathVariable @Min(1) Long reviewId,
            @Parameter(description = "新评价内容") @RequestParam String content,
            @Parameter(description = "新图片地址（逗号分隔）") @RequestParam String images) {
        return Result.success(productReviewService.updateReviewContent(productId, userId, reviewId, content, images));
    }

    @GetMapping("/user-latest")
    @Operation(summary = "获取用户最新评价", description = "查询用户最近的商品评价")
    @ApiResponse(responseCode = "200", description = "成功返回评价列表")
    public Result<List<ProductReview>> getUserLatestReviews(
            @Parameter(description = "用户ID", example = "1") @RequestParam @Min(1) Long userId,
            @Parameter(description = "返回数量", example = "5") @RequestParam(defaultValue = "5") @Min(1) Integer limit) {
        return Result.success(productReviewService.getLatestUserReviews(userId, limit));
    }
} 