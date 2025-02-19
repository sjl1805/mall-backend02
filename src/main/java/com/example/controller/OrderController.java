package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.order.OrderCreateDTO;
import com.example.model.entity.Orders;
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

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "订单管理接口")
public class OrderController {

    private final OrdersService ordersService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建订单", description = "用户提交新订单")
    @ApiResponse(responseCode = "201", description = "订单创建成功")
    public Result<String> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "订单信息",
                required = true,
                content = @Content(schema = @Schema(implementation = OrderCreateDTO.class))
            )
            @Valid @RequestBody OrderCreateDTO orderDTO) {
        return Result.success(ordersService.createOrder(orderDTO));
    }

    @GetMapping("/{orderNo}")
    @Operation(summary = "获取订单详情", description = "根据订单号查询订单详细信息")
    @ApiResponse(responseCode = "200", description = "成功返回订单详情")
    public Result<Orders> getOrderDetail(
            @Parameter(description = "订单号", example = "PC20230301123456") @PathVariable String orderNo) {
        return Result.success(ordersService.getOrderDetail(orderNo));
    }

    @PatchMapping("/{orderNo}/cancel")
    @Operation(summary = "取消订单", description = "用户取消未支付的订单")
    @ApiResponse(responseCode = "200", description = "订单取消成功")
    public Result<Boolean> cancelOrder(
            @Parameter(description = "订单号", example = "PC20230301123456") @PathVariable String orderNo) {
        return Result.success(ordersService.cancelOrder(orderNo));
    }

    @PatchMapping("/{orderNo}/pay")
    @Operation(summary = "支付订单", description = "用户支付订单")
    @ApiResponse(responseCode = "200", description = "支付成功")
    public Result<Boolean> payOrder(
            @Parameter(description = "订单号", example = "PC20230301123456") @PathVariable String orderNo) {
        return Result.success(ordersService.payOrder(orderNo));
    }
} 