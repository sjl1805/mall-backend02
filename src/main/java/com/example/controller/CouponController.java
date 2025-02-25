package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common. Result;
import com.example.common.ResultCode;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 优惠券控制器
 * <p>
 * 提供优惠券相关的API接口，包括优惠券的查询、创建、修改、删除等操作
 * 优惠券是电商系统中重要的营销工具，用于刺激消费、提高转化率和客户忠诚度
 */
@Tag(name = "Coupon", description = "优惠券管理API")
@RestController
@RequestMapping("/coupon")
@Validated
@Slf4j
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Operation(summary = "根据优惠券名称查询优惠券", description = "模糊匹配优惠券名称进行搜索")
    @GetMapping("/name/{name}")
    public  Result<List<Coupon>> getCouponsByName(
            @Parameter(description = "优惠券名称", required = true) @PathVariable String name) {
        log.info("根据优惠券名称查询优惠券请求: name={}", name);
        List<Coupon> coupons = couponService.selectByName(name);
        log.info("根据优惠券名称查询优惠券成功: name={}, count={}", name, coupons.size());
        return  Result.success(coupons);
    }

    @Operation(summary = "分页查询优惠券", description = "管理员分页查询所有优惠券")
    @GetMapping("/list")
    public  Result<IPage<Coupon>> getCouponList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询优惠券请求: page={}, size={}", page, size);
        IPage<Coupon> pageParam = new Page<>(page, size);
        IPage<Coupon> result = couponService.selectPage(pageParam);
        log.info("分页查询优惠券成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }

    @Operation(summary = "根据ID查询优惠券", description = "获取特定优惠券的详细信息")
    @GetMapping("/{id}")
    public  Result<Coupon> getCouponById(
            @Parameter(description = "优惠券ID", required = true) @PathVariable Long id) {
        log.info("根据ID查询优惠券请求: id={}", id);
        Coupon coupon = couponService.selectById(id);
        if (coupon != null) {
            log.info("根据ID查询优惠券成功: id={}", id);
            return  Result.success(coupon);
        } else {
            log.warn("根据ID查询优惠券失败: id={}, 优惠券不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "优惠券不存在");
        }
    }

    @Operation(summary = "新增优惠券", description = "创建新的优惠券")
    @PostMapping("/add")
    public  Result<Boolean> addCoupon(@Valid @RequestBody Coupon coupon) {
        log.info("新增优惠券请求: name={}, type={}, value={}", 
                coupon.getName(), coupon.getType(), coupon.getValue());
        
        // 参数验证
        if (coupon.getValue() != null && coupon.getValue().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("新增优惠券失败: 面值无效, value={}", coupon.getValue());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "优惠券面值不能为负数");
        }
        
        if (coupon.getNum() != null && coupon.getNum() <= 0) {
            log.warn("新增优惠券失败: 数量无效, num={}", coupon.getNum());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "优惠券数量必须大于0");
        }
        
        boolean result = couponService.insertCoupon(coupon);
        if (result) {
            log.info("新增优惠券成功: name={}, id={}", coupon.getName(), coupon.getId());
            return  Result.success(true);
        } else {
            log.warn("新增优惠券失败: name={}", coupon.getName());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新优惠券", description = "更新现有优惠券信息")
    @PutMapping("/update")
    public  Result<Boolean> updateCoupon(@Valid @RequestBody Coupon coupon) {
        log.info("更新优惠券请求: id={}", coupon.getId());
        
        // 参数验证
        if (coupon.getId() == null) {
            log.warn("更新优惠券失败: ID不能为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "优惠券ID不能为空");
        }
        
        if (coupon.getValue() != null && coupon.getValue().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("更新优惠券失败: 面值无效, value={}", coupon.getValue());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "优惠券面值不能为负数");
        }
        
        boolean result = couponService.updateCoupon(coupon);
        if (result) {
            log.info("更新优惠券成功: id={}", coupon.getId());
            return  Result.success(true);
        } else {
            log.warn("更新优惠券失败: id={}", coupon.getId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除优惠券", description = "删除指定的优惠券")
    @DeleteMapping("/{id}")
    public  Result<Boolean> deleteCoupon(
            @Parameter(description = "优惠券ID", required = true) @PathVariable Long id) {
        log.info("删除优惠券请求: id={}", id);
        
        // 检查优惠券是否存在
        Coupon coupon = couponService.selectById(id);
        if (coupon == null) {
            log.warn("删除优惠券失败: id={}, 优惠券不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "优惠券不存在");
        }
        
        boolean result = couponService.deleteCoupon(id);
        if (result) {
            log.info("删除优惠券成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除优惠券失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "设置优惠券有效期", description = "设置优惠券的开始和结束时间")
    @PutMapping("/setValidity/{id}")
    public  Result<Boolean> setCouponValidity(
            @Parameter(description = "优惠券ID", required = true) @PathVariable Long id,
            @Parameter(description = "开始时间，格式：yyyy-MM-dd HH:mm:ss", required = true) 
            @RequestParam String startTime,
            @Parameter(description = "结束时间，格式：yyyy-MM-dd HH:mm:ss", required = true) 
            @RequestParam String endTime) {
        log.info("设置优惠券有效期请求: id={}, startTime={}, endTime={}", id, startTime, endTime);
        
        // 检查优惠券是否存在
        Coupon coupon = couponService.selectById(id);
        if (coupon == null) {
            log.warn("设置优惠券有效期失败: id={}, 优惠券不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "优惠券不存在");
        }
        
        try {
            boolean result = couponService.setCouponValidity(id, startTime, endTime);
            if (result) {
                log.info("设置优惠券有效期成功: id={}", id);
                return  Result.success(true);
            } else {
                log.warn("设置优惠券有效期失败: id={}", id);
                return  Result.failed(ResultCode.FAILED);
            }
        } catch (Exception e) {
            log.error("设置优惠券有效期异常: id={}, error={}", id, e.getMessage(), e);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "日期格式错误，请使用yyyy-MM-dd HH:mm:ss格式");
        }
    }

    @Operation(summary = "设置使用条件", description = "设置优惠券的最低使用金额")
    @PutMapping("/setConditions/{id}")
    public  Result<Boolean> setCouponConditions(
            @Parameter(description = "优惠券ID", required = true) @PathVariable Long id,
            @Parameter(description = "最低使用金额", required = true) 
            @RequestParam BigDecimal minAmount) {
        log.info("设置使用条件请求: id={}, minAmount={}", id, minAmount);
        
        // 检查优惠券是否存在
        Coupon coupon = couponService.selectById(id);
        if (coupon == null) {
            log.warn("设置使用条件失败: id={}, 优惠券不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "优惠券不存在");
        }
        
        // 验证最低金额
        if (minAmount.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("设置使用条件失败: 最低金额无效, minAmount={}", minAmount);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "最低使用金额不能为负数");
        }
        
        boolean result = couponService.setCouponConditions(id, minAmount);
        if (result) {
            log.info("设置使用条件成功: id={}, minAmount={}", id, minAmount);
            return  Result.success(true);
        } else {
            log.warn("设置使用条件失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "查询可用优惠券", description = "查询适用于指定订单金额的可用优惠券")
    @GetMapping("/available")
    public  Result<List<Coupon>> getAvailableCoupons(
            @Parameter(description = "订单金额", required = true) 
            @RequestParam BigDecimal amount) {
        log.info("查询可用优惠券请求: amount={}", amount);
        
        // 参数验证
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("查询可用优惠券失败: 金额无效, amount={}", amount);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "订单金额不能为负数");
        }
        
        List<Coupon> coupons = couponService.selectAvailableCoupons(amount);
        log.info("查询可用优惠券成功: amount={}, count={}", amount, coupons.size());
        return  Result.success(coupons);
    }
    
    @Operation(summary = "查询即将过期的优惠券", description = "查询指定天数内即将过期的优惠券")
    @GetMapping("/expiring")
    public  Result<List<Coupon>> getExpiringCoupons(
            @Parameter(description = "过期天数（如7表示7天内过期）") 
            @RequestParam(defaultValue = "7") Integer days) {
        log.info("查询即将过期的优惠券请求: days={}", days);
        
        // 参数验证
        if (days <= 0) {
            log.warn("查询即将过期的优惠券失败: 天数无效, days={}", days);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "天数必须大于0");
        }
        
        List<Coupon> coupons = couponService.selectExpiringSoon(days);
        log.info("查询即将过期的优惠券成功: days={}, count={}", days, coupons.size());
        return  Result.success(coupons);
    }
    
    @Operation(summary = "减少优惠券数量", description = "减少优惠券可用数量（领取时调用）")
    @PutMapping("/{id}/decrease/{count}")
    public  Result<Boolean> decreaseCouponNum(
            @Parameter(description = "优惠券ID", required = true) @PathVariable Long id,
            @Parameter(description = "减少数量", required = true) @PathVariable Integer count) {
        log.info("减少优惠券数量请求: id={}, count={}", id, count);
        
        // 参数验证
        if (count <= 0) {
            log.warn("减少优惠券数量失败: 数量无效, count={}", count);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "减少数量必须大于0");
        }
        
        // 检查优惠券是否存在
        Coupon coupon = couponService.selectById(id);
        if (coupon == null) {
            log.warn("减少优惠券数量失败: id={}, 优惠券不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "优惠券不存在");
        }
        
        // 检查数量是否足够
        if (coupon.getNum() < count) {
            log.warn("减少优惠券数量失败: 库存不足, id={}, currentNum={}, count={}", id, coupon.getNum(), count);
            return  Result.failed(ResultCode.FAILED, "优惠券数量不足");
        }
        
        boolean result = couponService.decreaseCouponNum(id, count);
        if (result) {
            log.info("减少优惠券数量成功: id={}, count={}, newNum={}", id, count, coupon.getNum() - count);
            return  Result.success(true);
        } else {
            log.warn("减少优惠券数量失败: id={}, count={}", id, count);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "检查优惠券是否可用于指定金额", description = "验证优惠券是否可用于特定订单金额")
    @GetMapping("/{id}/check")
    public  Result<Coupon> checkCouponAvailable(
            @Parameter(description = "优惠券ID", required = true) @PathVariable Long id,
            @Parameter(description = "订单金额", required = true) @RequestParam BigDecimal amount) {
        log.info("检查优惠券是否可用于指定金额请求: id={}, amount={}", id, amount);
        
        // 参数验证
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("检查优惠券是否可用于指定金额失败: 金额无效, amount={}", amount);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "订单金额不能为负数");
        }
        
        Coupon coupon = couponService.checkCouponAvailable(id, amount);
        if (coupon != null) {
            log.info("检查优惠券是否可用于指定金额成功: id={}, amount={}, 可用", id, amount);
            return  Result.success(coupon);
        } else {
            log.info("检查优惠券是否可用于指定金额成功: id={}, amount={}, 不可用", id, amount);
            return  Result.failed(ResultCode.FAILED, "优惠券不可用于当前订单金额");
        }
    }
    
    @Operation(summary = "获取优惠券使用统计", description = "统计指定时间段内的优惠券使用情况")
    @GetMapping("/statistics")
    public  Result<List<Map<String, Object>>> getCouponStatistics(
            @Parameter(description = "开始日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Parameter(description = "结束日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        log.info("获取优惠券使用统计请求: startDate={}, endDate={}", startDate, endDate);
        
        // 验证时间范围
        if (startDate.after(endDate)) {
            log.warn("获取优惠券使用统计失败: 时间范围无效, startDate={}, endDate={}", startDate, endDate);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "开始日期不能晚于结束日期");
        }
        
        List<Map<String, Object>> statistics = couponService.getCouponStatistics(startDate, endDate);
        log.info("获取优惠券使用统计成功: count={}", statistics.size());
        return  Result.success(statistics);
    }
    
    @Operation(summary = "获取热门优惠券排行", description = "获取领取量最多的优惠券排行榜")
    @GetMapping("/popular")
    public  Result<List<Map<String, Object>>> getPopularCoupons(
            @Parameter(description = "限制数量") 
            @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取热门优惠券排行请求: limit={}", limit);
        
        // 参数验证
        if (limit <= 0) {
            log.warn("获取热门优惠券排行失败: 限制数量无效, limit={}", limit);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "限制数量必须大于0");
        }
        
        List<Map<String, Object>> popularCoupons = couponService.getPopularCoupons(limit);
        log.info("获取热门优惠券排行成功: count={}", popularCoupons.size());
        return  Result.success(popularCoupons);
    }
    
    @Operation(summary = "批量更新优惠券状态", description = "批量修改多个优惠券的状态")
    @PutMapping("/batch/status/{status}")
    public  Result<Boolean> batchUpdateStatus(
            @RequestBody List<Long> ids,
            @Parameter(description = "状态: 0-无效 1-有效", required = true) 
            @PathVariable Integer status) {
        log.info("批量更新优惠券状态请求: ids={}, status={}", ids, status);
        
        // 参数验证
        if (ids == null || ids.isEmpty()) {
            log.warn("批量更新优惠券状态失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "优惠券ID列表不能为空");
        }
        
        if (status != 0 && status != 1) {
            log.warn("批量更新优惠券状态失败: 状态无效, status={}", status);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "状态必须为0或1");
        }
        
        boolean result = couponService.batchUpdateStatus(ids, status);
        if (result) {
            log.info("批量更新优惠券状态成功: ids={}, status={}", ids, status);
            return  Result.success(true);
        } else {
            log.warn("批量更新优惠券状态失败: ids={}, status={}", ids, status);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "批量删除优惠券", description = "批量删除多个优惠券")
    @DeleteMapping("/batch")
    public  Result<Boolean> batchDelete(@RequestBody List<Long> ids) {
        log.info("批量删除优惠券请求: ids={}", ids);
        
        // 参数验证
        if (ids == null || ids.isEmpty()) {
            log.warn("批量删除优惠券失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "优惠券ID列表不能为空");
        }
        
        boolean result = couponService.batchDelete(ids);
        if (result) {
            log.info("批量删除优惠券成功: count={}", ids.size());
            return  Result.success(true);
        } else {
            log.warn("批量删除优惠券失败: ids={}", ids);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "查询用户未领取的优惠券", description = "获取当前有效且用户尚未领取的优惠券")
    @GetMapping("/notReceived/{userId}")
    public  Result<List<Coupon>> getNotReceivedCoupons(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("查询用户未领取的优惠券请求: userId={}", userId);
        List<Coupon> coupons = couponService.getNotReceivedCoupons(userId);
        log.info("查询用户未领取的优惠券成功: userId={}, count={}", userId, coupons.size());
        return  Result.success(coupons);
    }
    
    @Operation(summary = "按条件高级查询优惠券", description = "根据多条件组合查询优惠券")
    @PostMapping("/advancedSearch")
    public  Result<List<Coupon>> advancedSearch(@RequestBody Map<String, Object> params) {
        log.info("按条件高级查询优惠券请求: params={}", params);
        List<Coupon> coupons = couponService.advancedSearch(params);
        log.info("按条件高级查询优惠券成功: count={}", coupons.size());
        return  Result.success(coupons);
    }
    
    @Operation(summary = "计算优惠券发放效果", description = "统计优惠券的发放量、使用量、使用率等")
    @GetMapping("/effectiveness/{couponId}")
    public  Result<Map<String, Object>> calculateCouponEffectiveness(
            @Parameter(description = "优惠券ID", required = true) @PathVariable Long couponId) {
        log.info("计算优惠券发放效果请求: couponId={}", couponId);
        
        // 检查优惠券是否存在
        Coupon coupon = couponService.selectById(couponId);
        if (coupon == null) {
            log.warn("计算优惠券发放效果失败: couponId={}, 优惠券不存在", couponId);
            return  Result.failed(ResultCode.NOT_FOUND, "优惠券不存在");
        }
        
        Map<String, Object> effectiveness = couponService.calculateCouponEffectiveness(couponId);
        log.info("计算优惠券发放效果成功: couponId={}", couponId);
        return  Result.success(effectiveness);
    }
    
    @Operation(summary = "自动失效过期优惠券", description = "将已过期但状态仍为有效的优惠券状态更新为失效")
    @PostMapping("/expireOutdated")
    public  Result<Integer> expireOutdatedCoupons() {
        log.info("自动失效过期优惠券请求");
        int count = couponService.expireOutdatedCoupons();
        log.info("自动失效过期优惠券成功: count={}", count);
        return  Result.success(count);
    }
    
    @Operation(summary = "为用户推荐优惠券", description = "基于用户的历史记录，推荐合适的优惠券")
    @GetMapping("/recommend/{userId}")
    public  Result<List<Coupon>> recommendCouponsForUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "推荐数量") @RequestParam(defaultValue = "3") Integer limit) {
        log.info("为用户推荐优惠券请求: userId={}, limit={}", userId, limit);
        
        // 参数验证
        if (limit <= 0) {
            log.warn("为用户推荐优惠券失败: 限制数量无效, limit={}", limit);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "推荐数量必须大于0");
        }
        
        List<Coupon> coupons = couponService.recommendCouponsForUser(userId, limit);
        log.info("为用户推荐优惠券成功: userId={}, count={}", userId, coupons.size());
        return  Result.success(coupons);
    }
    
    @Operation(summary = "批量发放优惠券给用户", description = "将指定优惠券批量发放给多个用户")
    @PostMapping("/batchIssue")
    public Result<Integer> batchIssueCoupons(@RequestBody Map<String, Object> params) {
        Long couponId = Long.valueOf(params.get("couponId").toString());
        
        // 安全地转换 userIds
        Object userIdsObj = params.get("userIds");
        List<Long> userIds = new ArrayList<>();
        
        if (userIdsObj instanceof List<?>) {
            for (Object item : (List<?>) userIdsObj) {
                if (item instanceof Number) {
                    userIds.add(((Number) item).longValue());
                } else if (item instanceof String) {
                    try {
                        userIds.add(Long.valueOf((String) item));
                    } catch (NumberFormatException e) {
                        return Result.failed(ResultCode.VALIDATE_FAILED, "用户ID列表包含无效ID");
                    }
                }
            }
        } else {
            return Result.failed(ResultCode.VALIDATE_FAILED, "用户ID参数格式错误");
        }
        
        log.info("批量发放优惠券给用户请求: couponId={}, userCount={}", couponId, userIds.size());
        
        // 参数验证
        if (userIds.isEmpty()) {
            log.warn("批量发放优惠券给用户失败: 用户列表为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "用户ID列表不能为空");
        }
        
        // 检查优惠券是否存在
        Coupon coupon = couponService.selectById(couponId);
        if (coupon == null) {
            log.warn("批量发放优惠券给用户失败: couponId={}, 优惠券不存在", couponId);
            return  Result.failed(ResultCode.NOT_FOUND, "优惠券不存在");
        }
        
        // 检查优惠券数量是否足够
        if (coupon.getNum() < userIds.size()) {
            log.warn("批量发放优惠券给用户失败: 库存不足, couponId={}, currentNum={}, requiredNum={}", 
                    couponId, coupon.getNum(), userIds.size());
            return  Result.failed(ResultCode.FAILED, "优惠券数量不足，无法满足所有用户发放需求");
        }
        
        int successCount = couponService.batchIssueCoupons(couponId, userIds);
        log.info("批量发放优惠券给用户成功: couponId={}, totalUsers={}, successCount={}", 
                couponId, userIds.size(), successCount);
        
        if (successCount == 0) {
            return  Result.failed(ResultCode.FAILED, "发放失败，可能是优惠券已失效或数量不足");
        } else if (successCount < userIds.size()) {
            return  Result.success(successCount, "部分用户发放失败，可能已领取过该优惠券");
        } else {
            return  Result.success(successCount);
        }
    }
} 

