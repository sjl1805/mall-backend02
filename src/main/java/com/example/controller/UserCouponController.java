package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.UserCoupon;
import com.example.service.UserCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户优惠券控制器
 * 
 * 提供用户优惠券相关的API接口，包括查询、领取、使用等操作
 */
@Tag(name = "UserCoupon", description = "用户优惠券管理接口")
@RestController
@RequestMapping("/userCoupon")
public class UserCouponController {

    @Autowired
    private UserCouponService userCouponService;

    @Operation(summary = "根据用户ID查询用户优惠券")
    @GetMapping("/user/{userId}")
    public CommonResult<List<UserCoupon>> getCouponsByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        List<UserCoupon> coupons = userCouponService.selectByUserId(userId);
        return CommonResult.success(coupons);
    }

    @Operation(summary = "根据用户ID和状态查询用户优惠券")
    @GetMapping("/user/{userId}/status/{status}")
    public CommonResult<List<UserCoupon>> getCouponsByUserIdAndStatus(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId, 
            @Parameter(description = "优惠券状态：0-未使用 1-已使用 2-已过期", required = true) @PathVariable Integer status) {
        List<UserCoupon> coupons = userCouponService.selectByUserIdAndStatus(userId, status);
        return CommonResult.success(coupons);
    }
    
    @Operation(summary = "分页查询用户优惠券")
    @GetMapping("/page")
    public CommonResult<IPage<UserCoupon>> getUserCouponsByPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<UserCoupon> page = new Page<>(current, size);
        IPage<UserCoupon> result = userCouponService.selectPage(page);
        return CommonResult.success(result);
    }

    @Operation(summary = "根据ID查询用户优惠券详情")
    @GetMapping("/{id}")
    public CommonResult<UserCoupon> getUserCouponById(
            @Parameter(description = "用户优惠券ID", required = true) @PathVariable Long id) {
        UserCoupon userCoupon = userCouponService.selectById(id);
        return userCoupon != null ? CommonResult.success(userCoupon) : 
                CommonResult.failed(ResultCode.VALIDATE_FAILED, "用户优惠券不存在");
    }

    @Operation(summary = "新增用户优惠券（领取优惠券）")
    @PostMapping("/add")
    public CommonResult<Boolean> addUserCoupon(@Valid @RequestBody UserCoupon userCoupon) {
        boolean result = userCouponService.insertUserCoupon(userCoupon);
        return result ? CommonResult.success(true) : 
                CommonResult.failed(ResultCode.FAILED, "优惠券领取失败");
    }
    
    @Operation(summary = "用户领取指定优惠券")
    @PostMapping("/receive")
    public CommonResult<Boolean> receiveCoupon(
            @RequestBody Map<String, Long> params) {
        Long userId = params.get("userId");
        Long couponId = params.get("couponId");
        
        if (userId == null || couponId == null) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "用户ID和优惠券ID不能为空");
        }
        
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0); // 未使用状态
        
        boolean result = userCouponService.insertUserCoupon(userCoupon);
        return result ? CommonResult.success(true) : 
                CommonResult.failed(ResultCode.FAILED, "优惠券领取失败");
    }

    @Operation(summary = "更新用户优惠券")
    @PutMapping("/update")
    public CommonResult<Boolean> updateUserCoupon(@Valid @RequestBody UserCoupon userCoupon) {
        boolean result = userCouponService.updateUserCoupon(userCoupon);
        return result ? CommonResult.success(true) : 
                CommonResult.failed(ResultCode.FAILED, "优惠券更新失败");
    }
    
    @Operation(summary = "使用优惠券")
    @PutMapping("/{id}/use")
    public CommonResult<Boolean> useCoupon(
            @PathVariable Long id,
            @RequestParam(required = false) Long orderId) {
        UserCoupon userCoupon = userCouponService.selectById(id);
        if (userCoupon == null) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "优惠券不存在");
        }
        
        if (userCoupon.getStatus() != 0) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "优惠券已使用或已过期");
        }
        
        userCoupon.setStatus(1); // 设置为已使用
        userCoupon.setOrderId(orderId); // 关联订单ID
        
        boolean result = userCouponService.updateUserCoupon(userCoupon);
        return result ? CommonResult.success(true) : 
                CommonResult.failed(ResultCode.FAILED, "优惠券使用失败");
    }

    @Operation(summary = "根据ID删除用户优惠券")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteUserCoupon(
            @Parameter(description = "用户优惠券ID", required = true) @PathVariable Long id) {
        boolean result = userCouponService.deleteUserCoupon(id);
        return result ? CommonResult.success(true) : 
                CommonResult.failed(ResultCode.FAILED, "优惠券删除失败");
    }
    
    @Operation(summary = "批量删除用户优惠券")
    @DeleteMapping("/batch")
    public CommonResult<Boolean> batchDeleteUserCoupons(@RequestBody List<Long> ids) {
        boolean result = true;
        for (Long id : ids) {
            result = result && userCouponService.deleteUserCoupon(id);
        }
        return result ? CommonResult.success(true) : 
                CommonResult.failed(ResultCode.FAILED, "批量删除失败");
    }
    
    @Operation(summary = "查询用户可用优惠券数量")
    @GetMapping("/count/{userId}")
    public CommonResult<Integer> countUserAvailableCoupons(@PathVariable Long userId) {
        List<UserCoupon> availableCoupons = userCouponService.selectByUserIdAndStatus(userId, 0);
        return CommonResult.success(availableCoupons.size());
    }
    
    @Operation(summary = "检查优惠券是否可用")
    @GetMapping("/{id}/check")
    public CommonResult<Boolean> checkCouponAvailability(@PathVariable Long id) {
        UserCoupon userCoupon = userCouponService.selectById(id);
        boolean available = userCoupon != null && userCoupon.getStatus() == 0;
        return CommonResult.success(available);
    }
} 