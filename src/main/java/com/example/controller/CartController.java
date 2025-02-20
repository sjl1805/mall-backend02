package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.CartDTO;
import com.example.service.CartService;
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

/**
 * 购物车管理控制器
 * 
 * @author 31815
 * @description 提供购物车全生命周期管理功能，包含：
 *              1. 购物车商品管理
 *              2. 商品数量调整
 *              3. 购物车状态维护
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "购物车管理接口体系")
public class CartController {

    private final CartService cartService;

    /**
     * 添加商品到购物车（带库存校验）
     * @param userId 用户ID（必须大于0）
     * @param cartDTO 商品信息：
     *               - productId: 商品ID（必须）
     *               - quantity: 数量（必须，大于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - INSUFFICIENT_STOCK(17001): 库存不足
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "购物车商品添加", description = "用户添加商品到购物车接口")
    @ApiResponse(responseCode = "201", description = "商品添加成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "库存不足")
    public Result<Boolean> addToCart(
            @Parameter(description = "用户ID", example = "1", required = true)
            @RequestParam @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "购物车商品信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CartDTO.class))
            )
            @Valid @RequestBody CartDTO cartDTO) {
        return Result.success(cartService.addToCart(userId, cartDTO));
    }

    /**
     * 调整商品数量（事务操作）
     * @param userId 用户ID（必须大于0）
     * @param cartId 购物车项ID（路径参数，必须大于0）
     * @param delta 数量变化值（允许正负值）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - CART_ITEM_NOT_FOUND(17002): 购物车项不存在
     */
    @PatchMapping("/{cartId}/quantity")
    @Operation(summary = "商品数量调整", description = "修改购物车中商品的数量接口")
    @ApiResponse(responseCode = "200", description = "数量更新成功")
    @ApiResponse(responseCode = "404", description = "购物车项不存在")
    public Result<Boolean> updateQuantity(
            @Parameter(description = "用户ID", example = "1", required = true)
            @RequestParam @Min(1) Long userId,
            @Parameter(description = "购物车项ID", example = "1", required = true)
            @PathVariable @Min(1) Long cartId,
            @Parameter(description = "数量变化值", example = "2", required = true)
            @RequestParam @NotNull Integer delta) {
        return Result.success(cartService.updateQuantity(userId, cartId, delta));
    }

    /**
     * 批量勾选商品（事务操作）
     * @param userId 用户ID（必须大于0）
     * @param productIds 商品ID列表（最多50个）
     * @param checked 勾选状态（0-未选，1-已选）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - BATCH_LIMIT_EXCEEDED(17003): 超出批量处理限制
     */
    @PatchMapping("/batch-check")
    @Operation(summary = "批量勾选操作", description = "批量设置购物车项的选中状态接口")
    @ApiResponse(responseCode = "200", description = "勾选状态更新成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "413", description = "超出批量处理限制")
    public Result<Boolean> batchCheckItems(
            @Parameter(description = "用户ID", example = "1", required = true)
            @RequestParam @Min(1) Long userId,
            @Parameter(description = "商品ID列表", required = true)
            @RequestBody List<@Min(1) Long> productIds,
            @Parameter(description = "勾选状态", schema = @Schema(allowableValues = {"0", "1"}), required = true)
            @RequestParam @NotNull @Min(0) @Max(1) Integer checked) {
        return Result.success(cartService.batchCheckItems(userId, productIds, checked));
    }

    /**
     * 清空已选商品（事务操作）
     * @param userId 用户ID（必须大于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - CART_EMPTY(17004): 购物车为空
     */
    @DeleteMapping("/checked")
    @Operation(summary = "已选商品清空", description = "删除所有已勾选的购物车项接口")
    @ApiResponse(responseCode = "200", description = "清空成功")
    @ApiResponse(responseCode = "404", description = "购物车为空")
    public Result<Boolean> clearChecked(
            @Parameter(description = "用户ID", example = "1", required = true)
            @RequestParam @Min(1) Long userId) {
        return Result.success(cartService.clearChecked(userId));
    }

    /**
     * 获取购物车列表（带缓存）
     * @param userId 用户ID（必须大于0）
     * @return 购物车商品列表（按添加时间倒序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping
    @Operation(summary = "购物车列表查询", description = "查询用户的完整购物车信息接口")
    @ApiResponse(responseCode = "200", description = "成功返回购物车列表")
    public Result<List<CartDTO>> getCart(
            @Parameter(description = "用户ID", example = "1", required = true)
            @RequestParam @Min(1) Long userId) {
        return Result.success(cartService.getUserCart(userId));
    }

    /**
     * 获取购物车数量（带缓存）
     * @param userId 用户ID（必须大于0）
     * @return 商品总数
     * @implNote 结果缓存优化，有效期10分钟
     */
    @GetMapping("/count")
    @Operation(summary = "购物车数量统计", description = "查询用户的购物车商品总数接口")
    @ApiResponse(responseCode = "200", description = "成功返回商品数量")
    public Result<Integer> getCartItemCount(
            @Parameter(description = "用户ID", example = "1", required = true)
            @RequestParam @Min(1) Long userId) {
        return Result.success(cartService.getCartItemCount(userId));
    }

    /**
     * 删除购物车项（事务操作）
     * @param userId 用户ID（必须大于0）
     * @param cartIds 购物车项ID列表（最多50个）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - BATCH_LIMIT_EXCEEDED(17003): 超出批量处理限制
     */
    @DeleteMapping
    @Operation(summary = "购物车项删除", description = "批量删除购物车中的商品接口")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "413", description = "超出批量处理限制")
    public Result<Boolean> removeCartItems(
            @Parameter(description = "用户ID", example = "1", required = true)
            @RequestParam @Min(1) Long userId,
            @Parameter(description = "购物车项ID列表", required = true)
            @RequestBody List<@Min(1) Long> cartIds) {
        return Result.success(cartService.removeCartItems(userId, cartIds));
    }
} 