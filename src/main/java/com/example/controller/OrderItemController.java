package com.example.controller;

import com.example.common.Result;
import com.example.model.entity.OrderItem;
import com.example.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
@Tag(name = "OrderItem", description = "订单商品管理接口")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    @Operation(summary = "获取订单商品", description = "查询订单包含的商品明细")
    @ApiResponse(responseCode = "200", description = "成功返回商品列表")
    public Result<List<OrderItem>> getOrderItems(
            @Parameter(description = "订单ID", example = "1") @PathVariable @Min(1) Long orderId) {
        return Result.success(orderItemService.listByOrderId(orderId));
    }

    @PatchMapping("/{productId}/comment-status")
    @Operation(summary = "更新评价状态", description = "标记商品已完成评价")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    public Result<Boolean> updateCommentStatus(
            @Parameter(description = "订单ID", example = "1") @PathVariable @Min(1) Long orderId,
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId) {
        return Result.success(orderItemService.updateCommentStatus(orderId, productId));
    }
} 