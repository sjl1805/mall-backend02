package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common. Result;
import com.example.common.ResultCode;
import com.example.model.entity.OrderItem;
import com.example.service.OrderItemService;
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
 * 订单商品控制器
 * <p>
 * 提供订单商品相关的API接口，包括订单商品的查询、添加、修改、删除等操作
 * 订单商品是订单的组成部分，记录了订单中具体的商品信息
 */
@Tag(name = "OrderItem", description = "订单商品管理API")
@RestController
@RequestMapping("/orderItem")
@Validated
@Slf4j
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;
    

    @Operation(summary = "根据订单ID查询订单商品", description = "获取指定订单的所有商品")
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @ordersService.selectById(#orderId)?.userId == authentication.principal.id")
    public  Result<List<OrderItem>> getItemsByOrderId(
            @Parameter(description = "订单ID", required = true) @PathVariable Long orderId) {
        log.info("根据订单ID查询订单商品请求: orderId={}", orderId);
        List<OrderItem> items = orderItemService.selectByOrderId(orderId);
        log.info("根据订单ID查询订单商品成功: orderId={}, count={}", orderId, items.size());
        return  Result.success(items);
    }

    @Operation(summary = "分页查询订单商品", description = "管理员分页查询所有订单商品")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<IPage<OrderItem>> getOrderItemList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询订单商品请求: page={}, size={}", page, size);
        IPage<OrderItem> pageParam = new Page<>(page, size);
        IPage<OrderItem> result = orderItemService.selectPage(pageParam);
        log.info("分页查询订单商品成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }

    @Operation(summary = "根据ID查询订单商品", description = "获取特定订单商品的详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @orderItemService.selectById(#id)?.userId == authentication.principal.id")
    public  Result<OrderItem> getOrderItemById(
            @Parameter(description = "订单商品ID", required = true) @PathVariable Long id) {
        log.info("根据ID查询订单商品请求: id={}", id);
        OrderItem item = orderItemService.selectById(id);
        if (item != null) {
            log.info("根据ID查询订单商品成功: id={}", id);
            return  Result.success(item);
        } else {
            log.warn("根据ID查询订单商品失败: id={}, 订单商品不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "订单商品不存在");
        }
    }

    @Operation(summary = "新增订单商品", description = "添加新的订单商品")
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> addOrderItem(@Valid @RequestBody OrderItem orderItem) {
        log.info("新增订单商品请求: orderId={}, productId={}, quantity={}", 
                orderItem.getOrderId(), orderItem.getProductId(), orderItem.getQuantity());
        
        // 参数验证
        if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
            log.warn("新增订单商品失败: 数量无效, quantity={}", orderItem.getQuantity());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "商品数量必须大于0");
        }
        
        boolean result = orderItemService.insertOrderItem(orderItem);
        if (result) {
            log.info("新增订单商品成功: orderId={}, productId={}, id={}", 
                    orderItem.getOrderId(), orderItem.getProductId(), orderItem.getId());
            return  Result.success(true);
        } else {
            log.warn("新增订单商品失败: orderId={}, productId={}", 
                    orderItem.getOrderId(), orderItem.getProductId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新订单商品", description = "更新现有订单商品信息")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> updateOrderItem(@Valid @RequestBody OrderItem orderItem) {
        log.info("更新订单商品请求: id={}", orderItem.getId());
        
        // 参数验证
        if (orderItem.getId() == null) {
            log.warn("更新订单商品失败: ID不能为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "订单商品ID不能为空");
        }
        
        if (orderItem.getQuantity() != null && orderItem.getQuantity() <= 0) {
            log.warn("更新订单商品失败: 数量无效, quantity={}", orderItem.getQuantity());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "商品数量必须大于0");
        }
        
        boolean result = orderItemService.updateOrderItem(orderItem);
        if (result) {
            log.info("更新订单商品成功: id={}", orderItem.getId());
            return  Result.success(true);
        } else {
            log.warn("更新订单商品失败: id={}", orderItem.getId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除订单商品", description = "删除指定的订单商品")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> deleteOrderItem(
            @Parameter(description = "订单商品ID", required = true) @PathVariable Long id) {
        log.info("删除订单商品请求: id={}", id);
        
        // 检查订单商品是否存在
        OrderItem item = orderItemService.selectById(id);
        if (item == null) {
            log.warn("删除订单商品失败: id={}, 订单商品不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "订单商品不存在");
        }
        
        boolean result = orderItemService.deleteOrderItem(id);
        if (result) {
            log.info("删除订单商品成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除订单商品失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "批量插入订单项", description = "批量添加多个订单商品")
    @PostMapping("/batch/add")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> batchInsertOrderItems(@Valid @RequestBody List<OrderItem> orderItems) {
        log.info("批量插入订单项请求: count={}", orderItems.size());
        
        if (orderItems.isEmpty()) {
            log.warn("批量插入订单项失败: 订单项列表为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "订单项列表不能为空");
        }
        
        boolean result = orderItemService.batchInsertOrderItems(orderItems);
        if (result) {
            log.info("批量插入订单项成功: count={}", orderItems.size());
            return  Result.success(true);
        } else {
            log.warn("批量插入订单项失败: count={}", orderItems.size());
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "根据订单ID删除订单项", description = "删除指定订单的所有订单项")
    @DeleteMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> deleteByOrderId(
            @Parameter(description = "订单ID", required = true) @PathVariable Long orderId) {
        log.info("根据订单ID删除订单项请求: orderId={}", orderId);
        
        boolean result = orderItemService.deleteByOrderId(orderId);
        if (result) {
            log.info("根据订单ID删除订单项成功: orderId={}", orderId);
            return  Result.success(true);
        } else {
            log.warn("根据订单ID删除订单项失败: orderId={}", orderId);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "根据商品ID查询订单项", description = "获取包含指定商品的所有订单项")
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<List<OrderItem>> getItemsByProductId(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId) {
        log.info("根据商品ID查询订单项请求: productId={}", productId);
        List<OrderItem> items = orderItemService.selectByProductId(productId);
        log.info("根据商品ID查询订单项成功: productId={}, count={}", productId, items.size());
        return  Result.success(items);
    }
    
    @Operation(summary = "统计商品销量", description = "统计特定商品在指定时间段内的销量")
    @GetMapping("/product/{productId}/sales")
    public  Result<Integer> countProductSales(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId,
            @Parameter(description = "开始时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @Parameter(description = "结束时间") 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        log.info("统计商品销量请求: productId={}, startTime={}, endTime={}", productId, startTime, endTime);
        
        // 验证时间范围
        if (startTime != null && endTime != null && startTime.after(endTime)) {
            log.warn("统计商品销量失败: 时间范围无效, startTime={}, endTime={}", startTime, endTime);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "开始时间不能晚于结束时间");
        }
        
        int sales = orderItemService.countProductSales(productId, startTime, endTime);
        log.info("统计商品销量成功: productId={}, sales={}", productId, sales);
        return  Result.success(sales);
    }
    
    @Operation(summary = "查询热销商品", description = "获取指定时间段内销量最高的商品")
    @GetMapping("/hot")
    public  Result<List<Map<String, Object>>> getHotProducts(
            @Parameter(description = "开始时间") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @Parameter(description = "结束时间") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("查询热销商品请求: startTime={}, endTime={}, limit={}", startTime, endTime, limit);
        
        // 验证时间范围
        if (startTime.after(endTime)) {
            log.warn("查询热销商品失败: 时间范围无效, startTime={}, endTime={}", startTime, endTime);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "开始时间不能晚于结束时间");
        }
        
        if (limit <= 0) {
            log.warn("查询热销商品失败: 限制数量无效, limit={}", limit);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "限制数量必须大于0");
        }
        
        List<Map<String, Object>> hotProducts = orderItemService.selectHotProducts(startTime, endTime, limit);
        log.info("查询热销商品成功: count={}", hotProducts.size());
        return  Result.success(hotProducts);
    }
    
    @Operation(summary = "获取订单项详情", description = "获取订单项详情，包含商品信息")
    @GetMapping("/details/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @ordersService.selectById(#orderId)?.userId == authentication.principal.id")
    public  Result<List<Map<String, Object>>> getOrderItemsWithProductDetails(
            @Parameter(description = "订单ID", required = true) @PathVariable Long orderId) {
        log.info("获取订单项详情请求: orderId={}", orderId);
        List<Map<String, Object>> details = orderItemService.getOrderItemsWithProductDetails(orderId);
        log.info("获取订单项详情成功: orderId={}, count={}", orderId, details.size());
        return  Result.success(details);
    }
    
    @Operation(summary = "更新订单项的评价状态", description = "更新订单项的评价状态")
    @PutMapping("/{id}/review-status/{reviewStatus}")
    @PreAuthorize("hasRole('ADMIN') or @orderItemService.selectById(#id)?.userId == authentication.principal.id")
    public  Result<Boolean> updateReviewStatus(
            @Parameter(description = "订单项ID", required = true) @PathVariable Long id,
            @Parameter(description = "评价状态: 0-未评价 1-已评价", required = true) @PathVariable Integer reviewStatus) {
        log.info("更新订单项的评价状态请求: id={}, reviewStatus={}", id, reviewStatus);
        
        // 检查订单项是否存在
        OrderItem item = orderItemService.selectById(id);
        if (item == null) {
            log.warn("更新订单项的评价状态失败: id={}, 订单项不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "订单项不存在");
        }
        
        // 验证评价状态
        if (reviewStatus != 0 && reviewStatus != 1) {
            log.warn("更新订单项的评价状态失败: 评价状态无效, reviewStatus={}", reviewStatus);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "评价状态必须为0或1");
        }
        
        boolean result = orderItemService.updateReviewStatus(id, reviewStatus);
        if (result) {
            log.info("更新订单项的评价状态成功: id={}, reviewStatus={}", id, reviewStatus);
            return  Result.success(true);
        } else {
            log.warn("更新订单项的评价状态失败: id={}, reviewStatus={}", id, reviewStatus);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "更新订单项的退款状态", description = "更新订单项的退款状态")
    @PutMapping("/{id}/refund-status/{refundStatus}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> updateRefundStatus(
            @Parameter(description = "订单项ID", required = true) @PathVariable Long id,
            @Parameter(description = "退款状态: 0-未申请 1-申请中 2-已退款 3-已拒绝", required = true) @PathVariable Integer refundStatus) {
        log.info("更新订单项的退款状态请求: id={}, refundStatus={}", id, refundStatus);
        
        // 检查订单项是否存在
        OrderItem item = orderItemService.selectById(id);
        if (item == null) {
            log.warn("更新订单项的退款状态失败: id={}, 订单项不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "订单项不存在");
        }
        
        // 验证退款状态
        if (refundStatus < 0 || refundStatus > 3) {
            log.warn("更新订单项的退款状态失败: 退款状态无效, refundStatus={}", refundStatus);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "退款状态必须在0-3之间");
        }
        
        boolean result = orderItemService.updateRefundStatus(id, refundStatus);
        if (result) {
            log.info("更新订单项的退款状态成功: id={}, refundStatus={}", id, refundStatus);
            return  Result.success(true);
        } else {
            log.warn("更新订单项的退款状态失败: id={}, refundStatus={}", id, refundStatus);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "获取用户购买过的商品ID列表", description = "获取用户历史购买的商品ID列表")
    @GetMapping("/user/{userId}/products")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<Long>> getUserPurchasedProductIds(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "20") Integer limit) {
        log.info("获取用户购买过的商品ID列表请求: userId={}, limit={}", userId, limit);
        
        if (limit <= 0) {
            log.warn("获取用户购买过的商品ID列表失败: 限制数量无效, limit={}", limit);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "限制数量必须大于0");
        }
        
        List<Long> productIds = orderItemService.getUserPurchasedProductIds(userId, limit);
        log.info("获取用户购买过的商品ID列表成功: userId={}, count={}", userId, productIds.size());
        return  Result.success(productIds);
    }
    
    @Operation(summary = "获取订单项状态汇总", description = "获取订单项的状态统计数据")
    @GetMapping("/status-summary/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @ordersService.selectById(#orderId)?.userId == authentication.principal.id")
    public  Result<Map<String, Object>> getOrderItemStatusSummary(
            @Parameter(description = "订单ID", required = true) @PathVariable Long orderId) {
        log.info("获取订单项状态汇总请求: orderId={}", orderId);
        Map<String, Object> summary = orderItemService.getOrderItemStatusSummary(orderId);
        log.info("获取订单项状态汇总成功: orderId={}", orderId);
        return  Result.success(summary);
    }
} 

