package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Coupon", description = "优惠券的增删改查")
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Operation(summary = "根据优惠券名称查询优惠券")
    @GetMapping("/name/{name}")
    public CommonResult<List<Coupon>> getCouponsByName(@PathVariable String name) {
        List<Coupon> coupons = couponService.selectByName(name);
        return CommonResult.success(coupons);
    }

    @Operation(summary = "分页查询优惠券")
    @GetMapping("/list")
    public CommonResult<IPage<Coupon>> getCouponList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Coupon> pageParam = new Page<>(page, size);
        return CommonResult.success(couponService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询优惠券")
    @GetMapping("/{id}")
    public CommonResult<Coupon> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.selectById(id);
        return coupon != null ? CommonResult.success(coupon) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增优惠券")
    @PostMapping("/add")
    public CommonResult<Boolean> addCoupon(@Valid @RequestBody Coupon coupon) {
        boolean result = couponService.insertCoupon(coupon);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新优惠券")
    @PutMapping("/update")
    public CommonResult<Boolean> updateCoupon(@Valid @RequestBody Coupon coupon) {
        boolean result = couponService.updateCoupon(coupon);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除优惠券")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteCoupon(@PathVariable Long id) {
        boolean result = couponService.deleteCoupon(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "设置优惠券有效期")
    @PutMapping("/setValidity/{id}")
    public CommonResult<Boolean> setCouponValidity(
            @PathVariable Long id,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        boolean result = couponService.setCouponValidity(id, startTime, endTime);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "设置使用条件")
    @PutMapping("/setConditions/{id}")
    public CommonResult<Boolean> setCouponConditions(
            @PathVariable Long id,
            @RequestParam BigDecimal minAmount) {
        boolean result = couponService.setCouponConditions(id, minAmount);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 