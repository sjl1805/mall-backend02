package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.coupon.CouponDTO;
import com.example.model.dto.coupon.CouponPageDTO;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
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
@RequestMapping("/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupon", description = "优惠券管理接口")
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建优惠券", description = "管理员创建新的优惠券")
    @ApiResponse(responseCode = "201", description = "优惠券创建成功")
    public Result<Boolean> createCoupon(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "优惠券信息",
                required = true,
                content = @Content(schema = @Schema(implementation = CouponDTO.class))
            )
            @Valid @RequestBody CouponDTO couponDTO) {
        return Result.success(couponService.createCoupon(couponDTO));
    }

    @GetMapping
    @Operation(summary = "分页查询优惠券", description = "分页获取优惠券列表")
    @ApiResponse(responseCode = "200", description = "成功返回优惠券列表")
    public Result<IPage<Coupon>> listCoupons(
            @Parameter(description = "分页查询参数") @Valid CouponPageDTO queryDTO) {
        return Result.success(couponService.listCouponPage(queryDTO));
    }

    @PatchMapping("/{couponId}/status")
    @Operation(summary = "更新优惠券状态", description = "启用/禁用优惠券")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    public Result<Boolean> updateStatus(
            @Parameter(description = "优惠券ID", example = "1") @PathVariable @Min(1) Long couponId,
            @Parameter(description = "新状态：0-禁用 1-启用", example = "1") 
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(couponService.updateStatus(couponId, status));
    }

    @PostMapping("/expire")
    @Operation(summary = "过期优惠券处理", description = "系统定时任务处理过期优惠券")
    @ApiResponse(responseCode = "200", description = "处理完成")
    public Result<Boolean> expireCoupons() {
        return Result.success(couponService.expireCoupons());
    }

    @GetMapping("/available")
    @Operation(summary = "获取可用优惠券", description = "查询当前可领取的优惠券")
    @ApiResponse(responseCode = "200", description = "成功返回优惠券列表")
    public Result<List<Coupon>> getAvailableCoupons() {
        return Result.success(couponService.getAvailableCoupons());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户有效优惠券", description = "查询用户当前可用的优惠券")
    @ApiResponse(responseCode = "200", description = "成功返回优惠券列表")
    public Result<List<Coupon>> getUserValidCoupons(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId) {
        return Result.success(couponService.getUserValidCoupons(userId));
    }
} 