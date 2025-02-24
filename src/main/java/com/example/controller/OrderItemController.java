package com.example.controller;

import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.OrderItem;
import com.example.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@Tag(name = "订单商品管理", description = "订单商品的增删改查")
@RestController
@RequestMapping("/api/orderItem")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Operation(summary = "根据订单ID查询订单商品")
    @GetMapping("/order/{orderId}")
    public CommonResult<List<OrderItem>> getItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItem> items = orderItemService.selectByOrderId(orderId);
        return CommonResult.success(items);
    }

    @Operation(summary = "分页查询订单商品")
    @GetMapping("/list")
    public CommonResult<IPage<OrderItem>> getOrderItemList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<OrderItem> pageParam = new Page<>(page, size);
        return CommonResult.success(orderItemService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询订单商品")
    @GetMapping("/{id}")
    public CommonResult<OrderItem> getOrderItemById(@PathVariable Long id) {
        OrderItem item = orderItemService.selectById(id);
        return item != null ? CommonResult.success(item) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增订单商品")
    @PostMapping("/add")
    public CommonResult<Boolean> addOrderItem(@Valid @RequestBody OrderItem orderItem) {
        boolean result = orderItemService.insertOrderItem(orderItem);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新订单商品")
    @PutMapping("/update")
    public CommonResult<Boolean> updateOrderItem(@Valid @RequestBody OrderItem orderItem) {
        boolean result = orderItemService.updateOrderItem(orderItem);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除订单商品")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteOrderItem(@PathVariable Long id) {
        boolean result = orderItemService.deleteOrderItem(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 