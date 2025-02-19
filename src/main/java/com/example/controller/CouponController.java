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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券管理控制器
 * 
 * @author 31815
 * @description 提供优惠券全生命周期管理功能，包含：
 *              1. 优惠券发放与核销
 *              2. 优惠券状态管理
 *              3. 优惠券数据分析
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupon", description = "优惠券管理接口体系")
public class CouponController {

    private final CouponService couponService;

    /**
     * 创建优惠券（事务操作）
     * @param couponDTO 优惠券信息：
     *                 - couponName: 券名称（必须，最多20字）
     *                 - discountValue: 面值（必须，大于0）
     *                 - validDays: 有效期天数（必须）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - COUPON_DUPLICATE(15001): 重复优惠券名称
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "优惠券创建", description = "管理员创建新的优惠券接口")
    @ApiResponse(responseCode = "201", description = "优惠券创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复优惠券名称")
    public Result<Boolean> createCoupon(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "优惠券信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CouponDTO.class))
            )
            @Valid @RequestBody CouponDTO couponDTO) {
        return Result.success(couponService.createCoupon(couponDTO));
    }

    /**
     * 分页查询优惠券（带缓存）
     * @param queryDTO 分页参数：
     *                 - page: 当前页码（默认1）
     *                 - size: 每页数量（默认10，最大50）
     * @return 分页结果包装对象
     * @implNote 结果缓存优化，有效期5分钟
     */
    @GetMapping
    @Operation(summary = "优惠券分页查询", description = "分页获取优惠券列表接口")
    @ApiResponse(responseCode = "200", description = "成功返回优惠券列表")
    public Result<IPage<Coupon>> listCoupons(
            @Parameter(description = "分页查询参数") @Valid CouponPageDTO queryDTO) {
        return Result.success(couponService.listCouponPage(queryDTO));
    }

    /**
     * 更新优惠券状态（事务操作）
     * @param couponId 优惠券ID（路径参数，必须大于0）
     * @param status 新状态（0-禁用，1-启用）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - COUPON_NOT_FOUND(15002): 优惠券不存在
     */
    @PatchMapping("/{couponId}/status")
    @Operation(summary = "优惠券状态更新", description = "启用/禁用优惠券接口")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @ApiResponse(responseCode = "404", description = "优惠券不存在")
    public Result<Boolean> updateStatus(
            @Parameter(description = "优惠券ID", example = "1", required = true)
            @PathVariable @Min(1) Long couponId,
            @Parameter(description = "新状态", schema = @Schema(allowableValues = {"0", "1"}), required = true)
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(couponService.updateStatus(couponId, status));
    }

    /**
     * 过期优惠券处理（事务操作）
     * @return 处理结果
     * @implNote 系统定时任务调用，每天凌晨执行
     */
    @PostMapping("/expire")
    @Operation(summary = "过期优惠券处理", description = "系统定时任务处理过期优惠券接口")
    @ApiResponse(responseCode = "200", description = "处理完成")
    public Result<Boolean> expireCoupons() {
        return Result.success(couponService.expireCoupons());
    }

    /**
     * 获取可用优惠券（带缓存）
     * @return 可用优惠券列表（按创建时间倒序）
     * @implNote 结果缓存优化，有效期10分钟
     */
    @GetMapping("/available")
    @Operation(summary = "可用优惠券查询", description = "查询当前可领取的优惠券接口")
    @ApiResponse(responseCode = "200", description = "成功返回优惠券列表")
    public Result<List<Coupon>> getAvailableCoupons() {
        return Result.success(couponService.getAvailableCoupons());
    }

    /**
     * 获取用户有效优惠券（带缓存）
     * @param userId 用户ID（路径参数，必须大于0）
     * @return 有效优惠券列表（按过期时间升序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "用户优惠券查询", description = "查询用户当前可用的优惠券接口")
    @ApiResponse(responseCode = "200", description = "成功返回优惠券列表")
    public Result<List<Coupon>> getUserValidCoupons(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId) {
        return Result.success(couponService.getUserValidCoupons(userId));
    }
}