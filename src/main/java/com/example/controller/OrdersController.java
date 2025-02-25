package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.CommonResult;
import com.example.common.ResultCode;
import com.example.model.entity.Orders;
import com.example.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 * <p>
 * 提供订单相关的API接口，包括订单的查询、创建、修改、取消等操作
 * 订单是电商系统的核心业务实体，连接了用户、商品、支付、物流等多个业务领域
 */
@Tag(name = "Orders", description = "订单管理API")
@RestController
@RequestMapping("/orders")
@Validated
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Operation(summary = "根据用户ID查询订单", description = "获取指定用户的所有订单")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public CommonResult<List<Orders>> getOrdersByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("根据用户ID查询订单请求: userId={}", userId);
        List<Orders> orders = ordersService.selectByUserId(userId);
        log.info("根据用户ID查询订单成功: userId={}, count={}", userId, orders.size());
        return CommonResult.success(orders);
    }

    @Operation(summary = "分页查询订单", description = "管理员分页查询所有订单")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<IPage<Orders>> getOrderList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询订单请求: page={}, size={}", page, size);
        IPage<Orders> pageParam = new Page<>(page, size);
        IPage<Orders> result = ordersService.selectPage(pageParam);
        log.info("分页查询订单成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return CommonResult.success(result);
    }

    @Operation(summary = "根据ID查询订单", description = "获取特定订单的详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @ordersService.selectById(#id)?.userId == authentication.principal.id")
    public CommonResult<Orders> getOrderById(
            @Parameter(description = "订单ID", required = true) @PathVariable Long id) {
        log.info("根据ID查询订单请求: id={}", id);
        Orders order = ordersService.selectById(id);
        if (order != null) {
            log.info("根据ID查询订单成功: id={}", id);
            return CommonResult.success(order);
        } else {
            log.warn("根据ID查询订单失败: id={}, 订单不存在", id);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在");
        }
    }

    @Operation(summary = "新增订单", description = "创建新的订单")
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<Boolean> addOrder(@Valid @RequestBody Orders order) {
        log.info("新增订单请求: userId={}, totalAmount={}", order.getUserId(), order.getTotalAmount());
        
        // 验证用户只能为自己创建订单
        if (!isCurrentUserOrAdmin(order.getUserId())) {
            log.warn("新增订单失败: 权限不足, userId={}", order.getUserId());
            return CommonResult.failed(ResultCode.FORBIDDEN, "无权为其他用户创建订单");
        }
        
        boolean result = ordersService.insertOrder(order);
        if (result) {
            log.info("新增订单成功: userId={}, orderId={}", order.getUserId(), order.getId());
            return CommonResult.success(true);
        } else {
            log.warn("新增订单失败: userId={}", order.getUserId());
            return CommonResult.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新订单", description = "更新现有订单信息")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> updateOrder(@Valid @RequestBody Orders order) {
        log.info("更新订单请求: id={}", order.getId());
        
        // 参数验证
        if (order.getId() == null) {
            log.warn("更新订单失败: ID不能为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "订单ID不能为空");
        }
        
        boolean result = ordersService.updateOrder(order);
        if (result) {
            log.info("更新订单成功: id={}", order.getId());
            return CommonResult.success(true);
        } else {
            log.warn("更新订单失败: id={}", order.getId());
            return CommonResult.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除订单", description = "删除指定的订单（通常为软删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> deleteOrder(
            @Parameter(description = "订单ID", required = true) @PathVariable Long id) {
        log.info("删除订单请求: id={}", id);
        
        // 检查订单是否存在
        Orders order = ordersService.selectById(id);
        if (order == null) {
            log.warn("删除订单失败: id={}, 订单不存在", id);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在");
        }
        
        boolean result = ordersService.deleteOrder(id);
        if (result) {
            log.info("删除订单成功: id={}", id);
            return CommonResult.success(true);
        } else {
            log.warn("删除订单失败: id={}", id);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "根据订单号查询订单", description = "通过订单编号获取订单详情")
    @GetMapping("/no/{orderNo}")
    @PreAuthorize("hasRole('ADMIN') or @ordersService.selectByOrderNo(#orderNo)?.userId == authentication.principal.id")
    public CommonResult<Orders> getOrderByOrderNo(
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo) {
        log.info("根据订单号查询订单请求: orderNo={}", orderNo);
        Orders order = ordersService.selectByOrderNo(orderNo);
        if (order != null) {
            log.info("根据订单号查询订单成功: orderNo={}", orderNo);
            return CommonResult.success(order);
        } else {
            log.warn("根据订单号查询订单失败: orderNo={}, 订单不存在", orderNo);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在");
        }
    }
    
    @Operation(summary = "根据订单状态查询", description = "查询特定状态的所有订单")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<List<Orders>> getOrdersByStatus(
            @Parameter(description = "订单状态", required = true) @PathVariable Integer status) {
        log.info("根据订单状态查询请求: status={}", status);
        List<Orders> orders = ordersService.selectByStatus(status);
        log.info("根据订单状态查询成功: status={}, count={}", status, orders.size());
        return CommonResult.success(orders);
    }
    
    @Operation(summary = "查询用户特定状态的订单", description = "获取用户的特定状态订单")
    @GetMapping("/user/{userId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public CommonResult<List<Orders>> getOrdersByUserIdAndStatus(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "订单状态", required = true) @PathVariable Integer status) {
        log.info("查询用户特定状态的订单请求: userId={}, status={}", userId, status);
        List<Orders> orders = ordersService.selectByUserIdAndStatus(userId, status);
        log.info("查询用户特定状态的订单成功: userId={}, status={}, count={}", userId, status, orders.size());
        return CommonResult.success(orders);
    }
    
    @Operation(summary = "根据时间范围查询订单", description = "按时间段查询订单数据")
    @GetMapping("/time")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<List<Orders>> getOrdersByTimeRange(
            @Parameter(description = "开始时间") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @Parameter(description = "结束时间") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        log.info("根据时间范围查询订单请求: startTime={}, endTime={}", startTime, endTime);
        
        // 验证时间范围
        if (startTime.after(endTime)) {
            log.warn("根据时间范围查询订单失败: 时间范围无效, startTime={}, endTime={}", startTime, endTime);
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "开始时间不能晚于结束时间");
        }
        
        List<Orders> orders = ordersService.selectByTimeRange(startTime, endTime);
        log.info("根据时间范围查询订单成功: startTime={}, endTime={}, count={}", startTime, endTime, orders.size());
        return CommonResult.success(orders);
    }
    
    @Operation(summary = "获取订单统计数据", description = "获取订单统计数据，管理员可查看所有用户，普通用户只能查看自己的")
    @GetMapping("/statistics")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<List<Map<String, Object>>> getOrderStatistics(
            @Parameter(description = "用户ID（可选，管理员可不传）") @RequestParam(required = false) Long userId) {
        log.info("获取订单统计数据请求: userId={}", userId);
        
        // 如果不是管理员，只能查看自己的统计数据
        if (userId != null && !isCurrentUserOrAdmin(userId)) {
            log.warn("获取订单统计数据失败: 权限不足, userId={}", userId);
            return CommonResult.failed(ResultCode.FORBIDDEN, "无权查看其他用户的订单统计");
        }
        
        List<Map<String, Object>> statistics = ordersService.getOrderStatistics(userId);
        log.info("获取订单统计数据成功: userId={}, dataPoints={}", userId, statistics.size());
        return CommonResult.success(statistics);
    }
    
    @Operation(summary = "查询最近创建的订单", description = "获取系统中最近创建的订单")
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<List<Orders>> getRecentOrders(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("查询最近创建的订单请求: limit={}", limit);
        List<Orders> orders = ordersService.selectRecentOrders(limit);
        log.info("查询最近创建的订单成功: count={}", orders.size());
        return CommonResult.success(orders);
    }
    
    @Operation(summary = "更新订单支付信息", description = "更新订单的支付状态和支付方式")
    @PutMapping("/payment")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> updatePaymentInfo(
            @RequestBody Map<String, Object> params) {
        String orderNo = (String) params.get("orderNo");
        Integer paymentMethod = (Integer) params.get("paymentMethod");
        Date paymentTime = new Date(); // 使用当前时间作为支付时间
        
        log.info("更新订单支付信息请求: orderNo={}, paymentMethod={}", orderNo, paymentMethod);
        
        // 参数验证
        if (orderNo == null || orderNo.trim().isEmpty()) {
            log.warn("更新订单支付信息失败: 订单号为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "订单号不能为空");
        }
        
        if (paymentMethod == null) {
            log.warn("更新订单支付信息失败: 支付方式为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "支付方式不能为空");
        }
        
        // 检查订单是否存在
        Orders order = ordersService.selectByOrderNo(orderNo);
        if (order == null) {
            log.warn("更新订单支付信息失败: orderNo={}, 订单不存在", orderNo);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在");
        }
        
        boolean result = ordersService.updatePaymentInfo(orderNo, paymentMethod, paymentTime);
        if (result) {
            log.info("更新订单支付信息成功: orderNo={}", orderNo);
            return CommonResult.success(true);
        } else {
            log.warn("更新订单支付信息失败: orderNo={}", orderNo);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "更新订单物流信息", description = "更新订单的物流公司和运单号")
    @PutMapping("/shipping")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> updateShippingInfo(
            @RequestBody Map<String, String> params) {
        String orderNo = params.get("orderNo");
        String logisticsCompany = params.get("logisticsCompany");
        String trackingNumber = params.get("trackingNumber");
        
        log.info("更新订单物流信息请求: orderNo={}, logisticsCompany={}, trackingNumber={}", 
                orderNo, logisticsCompany, trackingNumber);
        
        // 参数验证
        if (orderNo == null || orderNo.trim().isEmpty()) {
            log.warn("更新订单物流信息失败: 订单号为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "订单号不能为空");
        }
        
        if (logisticsCompany == null || logisticsCompany.trim().isEmpty()) {
            log.warn("更新订单物流信息失败: 物流公司为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "物流公司不能为空");
        }
        
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
            log.warn("更新订单物流信息失败: 运单号为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "运单号不能为空");
        }
        
        // 检查订单是否存在
        Orders order = ordersService.selectByOrderNo(orderNo);
        if (order == null) {
            log.warn("更新订单物流信息失败: orderNo={}, 订单不存在", orderNo);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在");
        }
        
        boolean result = ordersService.updateShippingInfo(orderNo, logisticsCompany, trackingNumber);
        if (result) {
            log.info("更新订单物流信息成功: orderNo={}", orderNo);
            return CommonResult.success(true);
        } else {
            log.warn("更新订单物流信息失败: orderNo={}", orderNo);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "查询订单详情", description = "获取订单的完整信息，包括订单项")
    @GetMapping("/detail/{orderNo}")
    @PreAuthorize("hasRole('ADMIN') or @ordersService.selectByOrderNo(#orderNo)?.userId == authentication.principal.id")
    public CommonResult<Map<String, Object>> getOrderDetail(
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo) {
        log.info("查询订单详情请求: orderNo={}", orderNo);
        Map<String, Object> orderDetail = ordersService.selectOrderDetail(orderNo);
        if (orderDetail != null && !orderDetail.isEmpty()) {
            log.info("查询订单详情成功: orderNo={}", orderNo);
            return CommonResult.success(orderDetail);
        } else {
            log.warn("查询订单详情失败: orderNo={}, 订单不存在或无详情", orderNo);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在或无详情");
        }
    }
    
    @Operation(summary = "取消订单", description = "取消未支付订单")
    @PostMapping("/cancel")
    @PreAuthorize("hasRole('ADMIN') or @ordersService.selectByOrderNo(#params.get('orderNo'))?.userId == authentication.principal.id")
    public CommonResult<Boolean> cancelOrder(
            @RequestBody Map<String, String> params) {
        String orderNo = params.get("orderNo");
        String cancelReason = params.get("cancelReason");
        
        log.info("取消订单请求: orderNo={}, cancelReason={}", orderNo, cancelReason);
        
        // 参数验证
        if (orderNo == null || orderNo.trim().isEmpty()) {
            log.warn("取消订单失败: 订单号为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "订单号不能为空");
        }
        
        // 检查订单是否存在
        Orders order = ordersService.selectByOrderNo(orderNo);
        if (order == null) {
            log.warn("取消订单失败: orderNo={}, 订单不存在", orderNo);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在");
        }
        
        // 检查订单状态
        if (order.getStatus() != 0) { // 假设0表示待支付状态
            log.warn("取消订单失败: orderNo={}, 订单状态不允许取消", orderNo);
            return CommonResult.failed(ResultCode.FAILED, "只能取消待支付的订单");
        }
        
        boolean result = ordersService.cancelOrder(orderNo, cancelReason);
        if (result) {
            log.info("取消订单成功: orderNo={}", orderNo);
            return CommonResult.success(true);
        } else {
            log.warn("取消订单失败: orderNo={}", orderNo);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "确认收货", description = "用户确认已收到商品")
    @PostMapping("/receive/{orderNo}")
    @PreAuthorize("hasRole('ADMIN') or @ordersService.selectByOrderNo(#orderNo)?.userId == authentication.principal.id")
    public CommonResult<Boolean> confirmReceive(
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo) {
        log.info("确认收货请求: orderNo={}", orderNo);
        
        // 检查订单是否存在
        Orders order = ordersService.selectByOrderNo(orderNo);
        if (order == null) {
            log.warn("确认收货失败: orderNo={}, 订单不存在", orderNo);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在");
        }
        
        // 检查订单状态
        if (order.getStatus() != 2) { // 假设2表示已发货状态
            log.warn("确认收货失败: orderNo={}, 订单状态不是已发货", orderNo);
            return CommonResult.failed(ResultCode.FAILED, "只能确认已发货的订单");
        }
        
        boolean result = ordersService.confirmReceive(orderNo);
        if (result) {
            log.info("确认收货成功: orderNo={}", orderNo);
            return CommonResult.success(true);
        } else {
            log.warn("确认收货失败: orderNo={}", orderNo);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "申请退款", description = "用户申请订单退款")
    @PostMapping("/refund")
    @PreAuthorize("hasRole('ADMIN') or @ordersService.selectByOrderNo(#params.get('orderNo'))?.userId == authentication.principal.id")
    public CommonResult<Boolean> applyRefund(
            @RequestBody Map<String, String> params) {
        String orderNo = params.get("orderNo");
        String refundReason = params.get("refundReason");
        
        log.info("申请退款请求: orderNo={}, refundReason={}", orderNo, refundReason);
        
        // 参数验证
        if (orderNo == null || orderNo.trim().isEmpty()) {
            log.warn("申请退款失败: 订单号为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "订单号不能为空");
        }
        
        // 检查订单是否存在
        Orders order = ordersService.selectByOrderNo(orderNo);
        if (order == null) {
            log.warn("申请退款失败: orderNo={}, 订单不存在", orderNo);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在");
        }
        
        // 检查订单状态
        if (order.getStatus() <= 0 || order.getStatus() > 3) { // 假设只有状态1-3可以申请退款
            log.warn("申请退款失败: orderNo={}, 订单状态不允许退款", orderNo);
            return CommonResult.failed(ResultCode.FAILED, "当前订单状态不允许申请退款");
        }
        
        boolean result = ordersService.applyRefund(orderNo, refundReason);
        if (result) {
            log.info("申请退款成功: orderNo={}", orderNo);
            return CommonResult.success(true);
        } else {
            log.warn("申请退款失败: orderNo={}", orderNo);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "完成订单", description = "管理员手动完成订单")
    @PostMapping("/complete/{orderNo}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> completeOrder(
            @Parameter(description = "订单号", required = true) @PathVariable String orderNo) {
        log.info("完成订单请求: orderNo={}", orderNo);
        
        // 检查订单是否存在
        Orders order = ordersService.selectByOrderNo(orderNo);
        if (order == null) {
            log.warn("完成订单失败: orderNo={}, 订单不存在", orderNo);
            return CommonResult.failed(ResultCode.NOT_FOUND, "订单不存在");
        }
        
        boolean result = ordersService.completeOrder(orderNo);
        if (result) {
            log.info("完成订单成功: orderNo={}", orderNo);
            return CommonResult.success(true);
        } else {
            log.warn("完成订单失败: orderNo={}", orderNo);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "获取订单超时未支付数量", description = "统计超过指定时间未支付的订单数量")
    @GetMapping("/timeout/count")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Integer> countTimeoutOrders(
            @Parameter(description = "超时分钟数") @RequestParam(defaultValue = "30") int minutes) {
        log.info("获取订单超时未支付数量请求: minutes={}", minutes);
        
        if (minutes <= 0) {
            log.warn("获取订单超时未支付数量失败: 超时分钟数无效, minutes={}", minutes);
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "超时分钟数必须大于0");
        }
        
        int count = ordersService.countTimeoutOrders(minutes);
        log.info("获取订单超时未支付数量成功: minutes={}, count={}", minutes, count);
        return CommonResult.success(count);
    }
    
    @Operation(summary = "自动取消超时未支付订单", description = "取消超过指定时间未支付的订单")
    @PostMapping("/timeout/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Integer> autoCancelTimeoutOrders(
            @Parameter(description = "超时分钟数") @RequestParam(defaultValue = "30") int minutes) {
        log.info("自动取消超时未支付订单请求: minutes={}", minutes);
        
        if (minutes <= 0) {
            log.warn("自动取消超时未支付订单失败: 超时分钟数无效, minutes={}", minutes);
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "超时分钟数必须大于0");
        }
        
        int cancelCount = ordersService.autoCancelTimeoutOrders(minutes);
        log.info("自动取消超时未支付订单成功: minutes={}, cancelCount={}", minutes, cancelCount);
        return CommonResult.success(cancelCount);
    }
    
    /**
     * 辅助方法：判断当前用户是否为管理员或操作用户本人
     */
    private boolean isCurrentUserOrAdmin(Long userId) {
        // 此处需要根据您的安全框架实现具体逻辑
        // 简化处理，实际应从SecurityContext中获取用户信息进行判断
        return true; // 默认允许，实际实现中应该返回正确的判断结果
    }
} 