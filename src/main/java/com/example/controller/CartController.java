package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.Cart;
import com.example.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart", description = "购物车的增删改查")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "根据用户ID查询购物车")
    @GetMapping("/user/{userId}")
    public CommonResult<List<Cart>> getCartsByUserId(@PathVariable Long userId) {
        List<Cart> carts = cartService.selectByUserId(userId);
        return CommonResult.success(carts);
    }

    @Operation(summary = "分页查询购物车")
    @GetMapping("/list")
    public CommonResult<IPage<Cart>> getCartList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long userId) {
        IPage<Cart> pageParam = new Page<>(page, size);
        return CommonResult.success(cartService.selectPage(pageParam, userId));
    }

    @Operation(summary = "根据ID查询购物车")
    @GetMapping("/{id}")
    public CommonResult<Cart> getCartById(@PathVariable Long id) {
        Cart cart = cartService.selectById(id);
        return cart != null ? CommonResult.success(cart) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增购物车")
    @PostMapping("/add")
    public CommonResult<Boolean> addCart(@Valid @RequestBody Cart cart) {
        boolean result = cartService.insertCart(cart);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新购物车")
    @PutMapping("/update")
    public CommonResult<Boolean> updateCart(@Valid @RequestBody Cart cart) {
        boolean result = cartService.updateCart(cart);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除购物车")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteCart(@PathVariable Long id) {
        boolean result = cartService.deleteCart(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 