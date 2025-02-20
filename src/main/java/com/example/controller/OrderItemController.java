package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.OrderItemDTO;
import com.example.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单商品管理控制器
 * 
 * @author 31815
 * @description 提供订单商品全生命周期管理功能，包含：
 *              1. 订单商品明细查询
 *              2. 商品评价状态管理
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
@Tag(name = "OrderItem", description = "订单商品管理接口体系")
public class OrderItemController {

    private final OrderItemService orderItemService;

    /**
     * 获取订单商品明细（带缓存）
     * @param orderId 订单ID（路径参数，必须大于0）
     * @return 商品明细列表（按创建时间排序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping
    @Operation(summary = "订单商品查询", description = "查询订单包含的商品明细接口")
    @ApiResponse(responseCode = "200", description = "成功返回商品列表")
    @ApiResponse(responseCode = "404", description = "订单不存在")
    public Result<List<OrderItemDTO>> getOrderItems(
            @Parameter(description = "订单ID", example = "1", required = true)
            @PathVariable @Min(1) Long orderId) {
        return Result.success(orderItemService.listByOrderId(orderId));
    }

    /**
     * 更新评价状态（事务操作）
     * @param orderId 订单ID（路径参数，必须大于0）
     * @param productId 商品ID（路径参数，必须大于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - ORDER_ITEM_NOT_FOUND(11001): 订单商品不存在
     */
    @PatchMapping("/{productId}/comment-status")
    @Operation(summary = "评价状态更新", description = "标记商品已完成评价接口")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @ApiResponse(responseCode = "404", description = "订单商品不存在")
    public Result<Boolean> updateCommentStatus(
            @Parameter(description = "订单ID", example = "1", required = true)
            @PathVariable @Min(1) Long orderId,
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId) {
        return Result.success(orderItemService.updateCommentStatus(orderId, productId));
    }
} 