package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.OrderItem;

import java.util.List;

/**
 * 订单项服务接口
 */
public interface OrderItemService extends IService<OrderItem> {
    
    /**
     * 根据订单ID获取订单项列表
     *
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
    
    /**
     * 批量保存订单项
     *
     * @param orderItems 订单项列表
     * @return 是否保存成功
     */
    boolean saveBatchOrderItems(List<OrderItem> orderItems);
    
    /**
     * 统计商品销售数量
     *
     * @param productId 商品ID
     * @return 销售数量
     */
    int countProductSales(Long productId);
} 