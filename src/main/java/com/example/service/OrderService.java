package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Order;
import com.example.model.entity.OrderItem;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {
    
    /**
     * 创建订单
     *
     * @param userId 用户ID
     * @param addressId 收货地址ID
     * @param couponId 优惠券ID（可选）
     * @param note 订单备注（可选）
     * @return 创建成功的订单
     */
    Order createOrder(Long userId, Long addressId, Long couponId, String note);
    
    /**
     * 根据订单号查询订单
     *
     * @param orderNo 订单号
     * @return 订单对象
     */
    Order getOrderByOrderNo(String orderNo);
    
    /**
     * 获取用户订单列表
     *
     * @param userId 用户ID
     * @param page 分页参数
     * @return 分页订单列表
     */
    IPage<Order> getUserOrders(Long userId, Page<Order> page);
    
    /**
     * 获取订单详情项
     *
     * @param orderId 订单ID
     * @return 订单详情项列表
     */
    List<OrderItem> getOrderItems(Long orderId);
    
    /**
     * 取消订单
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 是否取消成功
     */
    boolean cancelOrder(Long userId, Long orderId);
    
    /**
     * 支付订单
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param paymentType 支付方式: 1-微信 2-支付宝
     * @return 是否支付成功
     */
    boolean payOrder(Long userId, Long orderId, Integer paymentType);
    
    /**
     * 确认收货
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 是否确认成功
     */
    boolean confirmOrder(Long userId, Long orderId);
    
    /**
     * 删除订单（逻辑删除）
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 是否删除成功
     */
    boolean deleteOrder(Long userId, Long orderId);
    
    /**
     * 更新订单状态
     *
     * @param orderId 订单ID
     * @param status 订单状态
     * @return 是否更新成功
     */
    boolean updateOrderStatus(Long orderId, Integer status);
    
    /**
     * 自动取消超时未支付订单
     *
     * @return 取消订单数量
     */
    int autoCancelTimeoutOrders();
} 