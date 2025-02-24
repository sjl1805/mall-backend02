package com.example.controller;

import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.UserCoupon;
import com.example.service.UserCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户优惠券管理", description = "用户优惠券的增删改查")
@RestController
@RequestMapping("/api/userCoupon")
public class UserCouponController {

    @Autowired
    private UserCouponService userCouponService;

    @Operation(summary = "根据用户ID查询用户优惠券")
    @GetMapping("/user/{userId}")
    public CommonResult<List<UserCoupon>> getCouponsByUserId(@PathVariable Long userId) {
        List<UserCoupon> coupons = userCouponService.selectByUserId(userId);
        return CommonResult.success(coupons);
    }

    @Operation(summary = "根据用户ID和状态查询用户优惠券")
    @GetMapping("/user/{userId}/status/{status}")
    public CommonResult<List<UserCoupon>> getCouponsByUserIdAndStatus(
            @PathVariable Long userId, @PathVariable String status) {
        List<UserCoupon> coupons = userCouponService.selectByUserIdAndStatus(userId, status);
        return CommonResult.success(coupons);
    }

    @Operation(summary = "新增用户优惠券")
    @PostMapping("/add")
    public CommonResult<Boolean> addUserCoupon(@Valid @RequestBody UserCoupon userCoupon) {
        boolean result = userCouponService.insertUserCoupon(userCoupon);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新用户优惠券")
    @PutMapping("/update")
    public CommonResult<Boolean> updateUserCoupon(@Valid @RequestBody UserCoupon userCoupon) {
        boolean result = userCouponService.updateUserCoupon(userCoupon);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除用户优惠券")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteUserCoupon(@PathVariable Long id) {
        boolean result = userCouponService.deleteUserCoupon(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 