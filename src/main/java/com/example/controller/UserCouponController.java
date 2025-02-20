package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.users.UserCouponDTO;
import com.example.service.UserCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户优惠券管理控制器
 * 
 * @author 31815
 * @description 提供用户优惠券全生命周期管理功能，包含：
 *              1. 优惠券领取与使用
 *              2. 优惠券状态管理
 *              3. 优惠券有效性校验
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/users/{userId}/coupons")
@RequiredArgsConstructor
@Tag(name = "UserCoupon", description = "用户优惠券管理接口体系")
public class UserCouponController {

    private final UserCouponService userCouponService;

    /**
     * 用户领取优惠券
     * @param userId 用户ID（路径参数，必须大于0）
     * @param couponId 优惠券ID（路径参数，必须大于0）
     * @return 操作结果：
     *         - true: 领取成功
     * @throws com.example.exception.BusinessException 可能异常：
     *         - COUPON_ALREADY_ACQUIRED(2001): 已领取该优惠券
     *         - COUPON_INVALID(2002): 优惠券已失效
     */
    @PostMapping("/{couponId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "领取优惠券", description = "用户领取指定优惠券接口")
    @ApiResponse(responseCode = "201", description = "领取成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复领取优惠券")
    public Result<Boolean> acquireCoupon(
            @Parameter(description = "用户ID", example = "1", required = true) 
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "优惠券ID", example = "1", required = true) 
            @PathVariable @Min(1) Long couponId) {
        return Result.success(userCouponService.acquireCoupon(userId, couponId));
    }

    /**
     * 使用优惠券（订单场景）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param userCouponId 用户优惠券ID（路径参数，必须大于0）
     * @param orderId 订单ID（查询参数，必须大于0）
     * @return 操作结果：
     *         - true: 使用成功
     * @throws com.example.exception.BusinessException 可能异常：
     *         - COUPON_NOT_FOUND(2003): 优惠券不存在
     *         - COUPON_USED(2004): 优惠券已使用
     *         - COUPON_EXPIRED(2005): 优惠券已过期
     */
    @PatchMapping("/{userCouponId}/use")
    @Operation(summary = "使用优惠券", description = "在订单中使用优惠券接口")
    @ApiResponse(responseCode = "200", description = "使用成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "404", description = "优惠券不存在")
    public Result<Boolean> useCoupon(
            @Parameter(description = "用户ID", example = "1", required = true) 
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "用户优惠券ID", example = "1", required = true) 
            @PathVariable @Min(1) Long userCouponId,
            @Parameter(description = "订单ID", example = "1", required = true) 
            @RequestParam @Min(1) Long orderId) {
        return Result.success(userCouponService.useCoupon(userId, userCouponId, orderId));
    }

    /**
     * 获取有效优惠券列表
     * @param userId 用户ID（路径参数，必须大于0）
     * @return 有效优惠券列表（按领取时间倒序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping("/valid")
    @Operation(summary = "有效优惠券查询", description = "获取用户可用优惠券列表")
    @ApiResponse(responseCode = "200", description = "成功返回优惠券列表")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    public Result<List<UserCouponDTO>> getValidCoupons(
            @Parameter(description = "用户ID", example = "1", required = true) 
            @PathVariable @Min(1) Long userId) {
        return Result.success(userCouponService.getValidCoupons(userId));
    }

    /**
     * 退还优惠券（订单取消场景）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param userCouponId 用户优惠券ID（路径参数，必须大于0）
     * @return 操作结果：
     *         - true: 退还成功
     * @throws com.example.exception.BusinessException 可能异常：
     *         - COUPON_CANNOT_RETURN(2006): 优惠券不可退还
     */
    @PatchMapping("/{userCouponId}/return")
    @Operation(summary = "退还优惠券", description = "取消订单后返还优惠券接口")
    @ApiResponse(responseCode = "200", description = "退还成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "优惠券不可退还")
    public Result<Boolean> returnCoupon(
            @Parameter(description = "用户ID", example = "1", required = true) 
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "用户优惠券ID", example = "1", required = true) 
            @PathVariable @Min(1) Long userCouponId) {
        return Result.success(userCouponService.returnCoupon(userId, userCouponId));
    }
} 