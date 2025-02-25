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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户优惠券控制器
 * <p>
 * 提供用户优惠券相关的API接口，包括查询、领取、使用等操作
 */
@Tag(name = "UserCoupon", description = "用户优惠券管理接口")
@RestController
@RequestMapping("/userCoupon")
@Validated
@Slf4j
public class UserCouponController {

    @Autowired
    private UserCouponService userCouponService;
    

    @Operation(summary = "根据用户ID查询用户优惠券", description = "获取指定用户的所有优惠券")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public CommonResult<List<UserCoupon>> getCouponsByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取用户优惠券列表请求: userId={}", userId);
        List<UserCoupon> coupons = userCouponService.selectByUserId(userId);
        log.info("获取用户优惠券成功: userId={}, count={}", userId, coupons.size());
        return CommonResult.success(coupons);
    }

    @Operation(summary = "根据用户ID和状态查询用户优惠券", description = "获取指定用户特定状态的优惠券")
    @GetMapping("/user/{userId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public CommonResult<List<UserCoupon>> getCouponsByUserIdAndStatus(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "优惠券状态：0-未使用 1-已使用 2-已过期", required = true) @PathVariable Integer status) {
        log.info("获取用户特定状态优惠券请求: userId={}, status={}", userId, status);
        
        // 验证状态参数
        if (status < 0 || status > 2) {
            log.warn("优惠券状态参数无效: status={}", status);
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "优惠券状态无效");
        }
        
        List<UserCoupon> coupons = userCouponService.selectByUserIdAndStatus(userId, status);
        log.info("获取用户特定状态优惠券成功: userId={}, status={}, count={}", userId, status, coupons.size());
        return CommonResult.success(coupons);
    }

    @Operation(summary = "分页查询用户优惠券", description = "管理员分页查询所有用户优惠券")
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<IPage<UserCoupon>> getUserCouponsByPage(
            @Parameter(description = "当前页码") 
            @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页记录数") 
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("分页查询用户优惠券请求: current={}, size={}", current, size);
        Page<UserCoupon> page = new Page<>(current, size);
        IPage<UserCoupon> result = userCouponService.selectPage(page);
        log.info("分页查询用户优惠券成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return CommonResult.success(result);
    }

    @Operation(summary = "根据ID查询用户优惠券详情", description = "获取特定优惠券的详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userCouponService.selectById(#id)?.userId == authentication.principal.id")
    public CommonResult<UserCoupon> getUserCouponById(
            @Parameter(description = "用户优惠券ID", required = true) @PathVariable Long id) {
        log.info("查询优惠券详情请求: id={}", id);
        UserCoupon userCoupon = userCouponService.selectById(id);
        if (userCoupon != null) {
            log.info("查询优惠券详情成功: id={}", id);
            return CommonResult.success(userCoupon);
        } else {
            log.warn("查询优惠券详情失败: id={}, 优惠券不存在", id);
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "用户优惠券不存在");
        }
    }

    @Operation(summary = "新增用户优惠券（领取优惠券）", description = "管理员添加用户优惠券记录")
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> addUserCoupon(@Valid @RequestBody UserCoupon userCoupon) {
        log.info("新增用户优惠券请求: userId={}, couponId={}", userCoupon.getUserId(), userCoupon.getCouponId());
        boolean result = userCouponService.insertUserCoupon(userCoupon);
        if (result) {
            log.info("新增用户优惠券成功: userId={}, couponId={}", userCoupon.getUserId(), userCoupon.getCouponId());
            return CommonResult.success(true);
        } else {
            log.warn("新增用户优惠券失败: userId={}, couponId={}", userCoupon.getUserId(), userCoupon.getCouponId());
            return CommonResult.failed(ResultCode.FAILED, "优惠券领取失败");
        }
    }

    @Operation(summary = "用户领取指定优惠券", description = "用户自主领取优惠券")
    @PostMapping("/receive")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #params.get('userId')")
    public CommonResult<Boolean> receiveCoupon(
            @RequestBody Map<String, Long> params) {
        Long userId = params.get("userId");
        Long couponId = params.get("couponId");
        
        log.info("用户领取优惠券请求: userId={}, couponId={}", userId, couponId);

        if (userId == null || couponId == null) {
            log.warn("用户领取优惠券失败: 参数无效, userId={}, couponId={}", userId, couponId);
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "用户ID和优惠券ID不能为空");
        }

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0); // 未使用状态

        boolean result = userCouponService.insertUserCoupon(userCoupon);
        if (result) {
            log.info("用户领取优惠券成功: userId={}, couponId={}", userId, couponId);
            return CommonResult.success(true);
        } else {
            log.warn("用户领取优惠券失败: userId={}, couponId={}", userId, couponId);
            return CommonResult.failed(ResultCode.FAILED, "优惠券领取失败");
        }
    }

    @Operation(summary = "更新用户优惠券", description = "管理员更新优惠券信息")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> updateUserCoupon(@Valid @RequestBody UserCoupon userCoupon) {
        log.info("更新用户优惠券请求: id={}", userCoupon.getId());
        boolean result = userCouponService.updateUserCoupon(userCoupon);
        if (result) {
            log.info("更新用户优惠券成功: id={}", userCoupon.getId());
            return CommonResult.success(true);
        } else {
            log.warn("更新用户优惠券失败: id={}", userCoupon.getId());
            return CommonResult.failed(ResultCode.FAILED, "优惠券更新失败");
        }
    }

    @Operation(summary = "使用优惠券", description = "用户使用优惠券")
    @PutMapping("/{id}/use")
    @PreAuthorize("hasRole('ADMIN') or @userCouponService.selectById(#id)?.userId == authentication.principal.id")
    public CommonResult<Boolean> useCoupon(
            @PathVariable Long id,
            @RequestParam(required = false) Long orderId) {
        log.info("使用优惠券请求: id={}, orderId={}", id, orderId);
        
        UserCoupon userCoupon = userCouponService.selectById(id);
        if (userCoupon == null) {
            log.warn("使用优惠券失败: id={}, 优惠券不存在", id);
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "优惠券不存在");
        }

        if (userCoupon.getStatus() != 0) {
            log.warn("使用优惠券失败: id={}, 优惠券状态无效, status={}", id, userCoupon.getStatus());
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "优惠券已使用或已过期");
        }

        userCoupon.setStatus(1); // 设置为已使用
        userCoupon.setOrderId(orderId); // 关联订单ID

        boolean result = userCouponService.updateUserCoupon(userCoupon);
        if (result) {
            log.info("使用优惠券成功: id={}, orderId={}", id, orderId);
            return CommonResult.success(true);
        } else {
            log.warn("使用优惠券失败: id={}, orderId={}", id, orderId);
            return CommonResult.failed(ResultCode.FAILED, "优惠券使用失败");
        }
    }

    @Operation(summary = "根据ID删除用户优惠券", description = "管理员删除优惠券记录")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> deleteUserCoupon(
            @Parameter(description = "用户优惠券ID", required = true) @PathVariable Long id) {
        log.info("删除用户优惠券请求: id={}", id);
        boolean result = userCouponService.deleteUserCoupon(id);
        if (result) {
            log.info("删除用户优惠券成功: id={}", id);
            return CommonResult.success(true);
        } else {
            log.warn("删除用户优惠券失败: id={}", id);
            return CommonResult.failed(ResultCode.FAILED, "优惠券删除失败");
        }
    }

    @Operation(summary = "批量删除用户优惠券", description = "管理员批量删除优惠券记录")
    @DeleteMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> batchDeleteUserCoupons(@RequestBody List<Long> ids) {
        log.info("批量删除用户优惠券请求: ids={}", ids);
        boolean result = true;
        for (Long id : ids) {
            result = result && userCouponService.deleteUserCoupon(id);
        }
        if (result) {
            log.info("批量删除用户优惠券成功: ids={}", ids);
            return CommonResult.success(true);
        } else {
            log.warn("批量删除用户优惠券失败: ids={}", ids);
            return CommonResult.failed(ResultCode.FAILED, "批量删除失败");
        }
    }

    @Operation(summary = "查询用户可用优惠券数量", description = "获取用户未使用的优惠券数量")
    @GetMapping("/count/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public CommonResult<Integer> countUserAvailableCoupons(@PathVariable Long userId) {
        log.info("查询用户可用优惠券数量请求: userId={}", userId);
        List<UserCoupon> availableCoupons = userCouponService.selectByUserIdAndStatus(userId, 0);
        log.info("查询用户可用优惠券数量成功: userId={}, count={}", userId, availableCoupons.size());
        return CommonResult.success(availableCoupons.size());
    }

    @Operation(summary = "检查优惠券是否可用", description = "验证优惠券是否可用于下单")
    @GetMapping("/{id}/check")
    @PreAuthorize("hasRole('ADMIN') or @userCouponService.selectById(#id)?.userId == authentication.principal.id")
    public CommonResult<Boolean> checkCouponAvailability(@PathVariable Long id) {
        log.info("检查优惠券可用性请求: id={}", id);
        UserCoupon userCoupon = userCouponService.selectById(id);
        boolean available = userCoupon != null && userCoupon.getStatus() == 0;
        log.info("检查优惠券可用性结果: id={}, available={}", id, available);
        return CommonResult.success(available);
    }
} 