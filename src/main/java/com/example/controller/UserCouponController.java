package com.example.controller;

import com.example.common.Result;
import com.example.model.entity.UserCoupon;
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

@RestController
@RequestMapping("/users/{userId}/coupons")
@RequiredArgsConstructor
@Tag(name = "UserCoupon", description = "用户优惠券管理接口")
public class UserCouponController {

    private final UserCouponService userCouponService;

    @PostMapping("/{couponId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "领取优惠券", description = "用户领取指定优惠券")
    @ApiResponse(responseCode = "201", description = "领取成功")
    public Result<Boolean> acquireCoupon(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "优惠券ID", example = "1") @PathVariable @Min(1) Long couponId) {
        return Result.success(userCouponService.acquireCoupon(userId, couponId));
    }

    @PatchMapping("/{userCouponId}/use")
    @Operation(summary = "使用优惠券", description = "在订单中使用优惠券")
    @ApiResponse(responseCode = "200", description = "使用成功")
    public Result<Boolean> useCoupon(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "用户优惠券ID", example = "1") @PathVariable @Min(1) Long userCouponId,
            @Parameter(description = "订单ID", example = "1") @RequestParam @Min(1) Long orderId) {
        return Result.success(userCouponService.useCoupon(userId, userCouponId, orderId));
    }

    @GetMapping("/valid")
    @Operation(summary = "获取有效优惠券", description = "查询用户可用的优惠券列表")
    @ApiResponse(responseCode = "200", description = "成功返回优惠券列表")
    public Result<List<UserCoupon>> getValidCoupons(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId) {
        return Result.success(userCouponService.getValidCoupons(userId));
    }

    @PatchMapping("/{userCouponId}/return")
    @Operation(summary = "退还优惠券", description = "取消订单后返还优惠券")
    @ApiResponse(responseCode = "200", description = "退还成功")
    public Result<Boolean> returnCoupon(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "用户优惠券ID", example = "1") @PathVariable @Min(1) Long userCouponId) {
        return Result.success(userCouponService.returnCoupon(userId, userCouponId));
    }
} 