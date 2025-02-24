package com.example.controller;

import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.Orders;
import com.example.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@Tag(name = "订单管理", description = "订单的增删改查")
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Operation(summary = "根据用户ID查询订单")
    @GetMapping("/user/{userId}")
    public CommonResult<List<Orders>> getOrdersByUserId(@PathVariable Long userId) {
        List<Orders> orders = ordersService.selectByUserId(userId);
        return CommonResult.success(orders);
    }

    @Operation(summary = "分页查询订单")
    @GetMapping("/list")
    public CommonResult<IPage<Orders>> getOrderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Orders> pageParam = new Page<>(page, size);
        return CommonResult.success(ordersService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询订单")
    @GetMapping("/{id}")
    public CommonResult<Orders> getOrderById(@PathVariable Long id) {
        Orders order = ordersService.selectById(id);
        return order != null ? CommonResult.success(order) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增订单")
    @PostMapping("/add")
    public CommonResult<Boolean> addOrder(@Valid @RequestBody Orders order) {
        boolean result = ordersService.insertOrder(order);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新订单")
    @PutMapping("/update")
    public CommonResult<Boolean> updateOrder(@Valid @RequestBody Orders order) {
        boolean result = ordersService.updateOrder(order);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除订单")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteOrder(@PathVariable Long id) {
        boolean result = ordersService.deleteOrder(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 