package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.order.OrderCreateDTO;
import com.example.model.dto.order.OrdersDTO;
import com.example.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理控制器
 * 
 * @author 31815
 * @description 提供订单全生命周期管理功能，包含：
 *              1. 订单创建与支付
 *              2. 订单状态管理
 *              3. 订单详情查询
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "订单管理接口体系")
public class OrderController {

    private final OrdersService ordersService;

    /**
     * 创建订单（事务操作）
     * @param orderDTO 订单信息：
     *                - userId: 用户ID（必须）
     *                - items: 订单商品列表（必须）
     *                - addressId: 收货地址ID（必须）
     * @return 订单号
     * @throws com.example.exception.BusinessException 可能异常：
     *         - INSUFFICIENT_STOCK(12001): 库存不足
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "订单创建", description = "用户提交新订单接口")
    @ApiResponse(responseCode = "201", description = "订单创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "库存不足")
    public Result<String> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "订单信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderCreateDTO.class))
            )
            @Valid @RequestBody OrderCreateDTO orderDTO) {
        return Result.success(ordersService.createOrder(orderDTO));
    }

    /**
     * 获取订单详情（带缓存）
     * @param orderNo 订单号（路径参数）
     * @return 订单详情（包含商品明细）
     * @implNote 结果缓存优化，有效期1小时
     */
    @GetMapping("/{orderNo}")
    @Operation(summary = "订单详情查询", description = "根据订单号查询订单详细信息接口")
    @ApiResponse(responseCode = "200", description = "成功返回订单详情")
    @ApiResponse(responseCode = "404", description = "订单不存在")
    public Result<OrdersDTO> getOrderDetail(
            @Parameter(description = "订单号", example = "202309010001", required = true)
            @PathVariable String orderNo) {
        return Result.success(ordersService.getOrderDetail(orderNo));
    }

    /**
     * 取消订单（事务操作）
     * @param orderNo 订单号（路径参数）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - ORDER_CANCEL_FAILED(12002): 订单取消失败
     */
    @PatchMapping("/{orderNo}/cancel")
    @Operation(summary = "订单取消", description = "用户取消未支付的订单接口")
    @ApiResponse(responseCode = "200", description = "订单取消成功")
    @ApiResponse(responseCode = "400", description = "订单状态不合法")
    @ApiResponse(responseCode = "404", description = "订单不存在")
    public Result<Boolean> cancelOrder(
            @Parameter(description = "订单号", example = "PC20230301123456", required = true)
            @PathVariable String orderNo) {
        return Result.success(ordersService.cancelOrder(orderNo));
    }

    /**
     * 支付订单（事务操作）
     * @param orderNo 订单号（路径参数）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - ORDER_PAY_FAILED(12003): 订单支付失败
     */
    @PatchMapping("/{orderNo}/pay")
    @Operation(summary = "订单支付", description = "用户支付订单接口")
    @ApiResponse(responseCode = "200", description = "支付成功")
    @ApiResponse(responseCode = "400", description = "订单状态不合法")
    @ApiResponse(responseCode = "404", description = "订单不存在")
    public Result<Boolean> payOrder(
            @Parameter(description = "订单号", example = "PC20230301123456", required = true)
            @PathVariable String orderNo) {
        return Result.success(ordersService.payOrder(orderNo));
    }
} 