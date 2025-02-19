package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.cart.CartDTO;
import com.example.model.entity.Cart;
import com.example.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "购物车管理接口")
public class CartController {

    private final CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "添加商品到购物车", description = "用户添加商品到购物车")
    @ApiResponse(responseCode = "201", description = "商品添加成功")
    public Result<Boolean> addToCart(
            @Parameter(description = "用户ID", example = "1") @RequestParam @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "购物车商品信息",
                required = true,
                content = @Content(schema = @Schema(implementation = CartDTO.class))
            )
            @Valid @RequestBody CartDTO cartDTO) {
        return Result.success(cartService.addToCart(userId, cartDTO));
    }

    @PatchMapping("/{cartId}/quantity")
    @Operation(summary = "调整商品数量", description = "修改购物车中商品的数量")
    @ApiResponse(responseCode = "200", description = "数量更新成功")
    public Result<Boolean> updateQuantity(
            @Parameter(description = "用户ID", example = "1") @RequestParam @Min(1) Long userId,
            @Parameter(description = "购物车项ID", example = "1") @PathVariable @Min(1) Long cartId,
            @Parameter(description = "数量变化值（正数增加，负数减少）", example = "2") 
            @RequestParam @NotNull Integer delta) {
        return Result.success(cartService.updateQuantity(userId, cartId, delta));
    }

    @PatchMapping("/batch-check")
    @Operation(summary = "批量勾选商品", description = "批量设置购物车项的选中状态")
    @ApiResponse(responseCode = "200", description = "勾选状态更新成功")
    public Result<Boolean> batchCheckItems(
            @Parameter(description = "用户ID", example = "1") @RequestParam @Min(1) Long userId,
            @Parameter(description = "商品ID列表") @RequestBody List<@Min(1) Long> productIds,
            @Parameter(description = "勾选状态：0-未选 1-已选", example = "1") 
            @RequestParam @NotNull @Min(0) @Max(1) Integer checked) {
        return Result.success(cartService.batchCheckItems(userId, productIds, checked));
    }

    @DeleteMapping("/checked")
    @Operation(summary = "清空已选商品", description = "删除所有已勾选的购物车项")
    @ApiResponse(responseCode = "200", description = "清空成功")
    public Result<Boolean> clearChecked(
            @Parameter(description = "用户ID", example = "1") @RequestParam @Min(1) Long userId) {
        return Result.success(cartService.clearChecked(userId));
    }

    @GetMapping
    @Operation(summary = "获取购物车列表", description = "查询用户的完整购物车信息")
    @ApiResponse(responseCode = "200", description = "成功返回购物车列表")
    public Result<List<Cart>> getCart(
            @Parameter(description = "用户ID", example = "1") @RequestParam @Min(1) Long userId) {
        return Result.success(cartService.getUserCart(userId));
    }

    @GetMapping("/count")
    @Operation(summary = "获取购物车数量", description = "查询用户的购物车商品总数")
    @ApiResponse(responseCode = "200", description = "成功返回商品数量")
    public Result<Integer> getCartItemCount(
            @Parameter(description = "用户ID", example = "1") @RequestParam @Min(1) Long userId) {
        return Result.success(cartService.getCartItemCount(userId));
    }

    @DeleteMapping
    @Operation(summary = "删除购物车项", description = "批量删除购物车中的商品")
    @ApiResponse(responseCode = "200", description = "删除成功")
    public Result<Boolean> removeCartItems(
            @Parameter(description = "用户ID", example = "1") @RequestParam @Min(1) Long userId,
            @Parameter(description = "购物车项ID列表") @RequestBody List<@Min(1) Long> cartIds) {
        return Result.success(cartService.removeCartItems(userId, cartIds));
    }
} 