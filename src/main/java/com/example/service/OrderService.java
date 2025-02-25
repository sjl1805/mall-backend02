package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建订单
     * @param order 订单信息
     * @return 订单号
     */
    String createOrder(Order order);
    
    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    Order getOrderByOrderNo(String orderNo);
    
    /**
     * 获取用户订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> getUserOrders(Long userId);
    
    /**
     * 获取用户指定状态的订单
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> getUserOrdersByStatus(Long userId, Integer status);
    
    /**
     * 分页查询用户订单
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 订单状态，可选参数
     * @return 分页订单数据
     */
    IPage<Order> getUserOrderPage(Page<Order> page, Long userId, Integer status);
    
    /**
     * 更新订单状态
     * @param orderNo 订单号
     * @param oldStatus 原状态
     * @param newStatus 新状态
     * @return 是否更新成功
     */
    boolean updateOrderStatus(String orderNo, Integer oldStatus, Integer newStatus);
    
    /**
     * 取消订单
     * @param orderNo 订单号
     * @param userId 用户ID（用于验证权限）
     * @return 是否取消成功
     */
    boolean cancelOrder(String orderNo, Long userId);
    
    /**
     * 支付订单
     * @param orderNo 订单号
     * @return 是否支付成功
     */
    boolean payOrder(String orderNo);
    
    /**
     * 订单发货
     * @param orderNo 订单号
     * @return 是否发货成功
     */
    boolean shipOrder(String orderNo);
    
    /**
     * 确认收货
     * @param orderNo 订单号
     * @param userId 用户ID（用于验证权限）
     * @return 是否确认成功
     */
    boolean confirmReceive(String orderNo, Long userId);
    
    /**
     * 统计用户各状态订单数量
     * @param userId 用户ID
     * @return 各状态订单数量Map
     */
    Map<String, Integer> countUserOrderStatus(Long userId);
    
    /**
     * 处理超时未支付订单
     * @param timeoutMinutes 超时分钟数
     * @return 处理的订单数量
     */
    int handleTimeoutOrders(int timeoutMinutes);
    
    /**
     * 根据时间范围和状态查询订单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> getOrdersByTimeRangeAndStatus(LocalDateTime startTime, LocalDateTime endTime, Integer status);
} 