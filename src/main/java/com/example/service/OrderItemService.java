package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.OrderItemDTO;
import com.example.model.entity.OrderItem;

import java.util.List;

/**
 * 订单项服务接口
 * 
 * @author 31815
 * @description 提供订单商品项管理功能，包含：
 *              1. 订单项查询
 *              2. 评价状态管理
 * @createDate 2025-02-18 23:44:21
 */
public interface OrderItemService extends IService<OrderItem> {

    /**
     * 根据订单ID查询订单项
     * @param orderId 订单ID
     * @return 订单项列表（包含商品详情）
     * @implNote 用于订单详情页展示
     */
    List<OrderItemDTO> listByOrderId(Long orderId);

    /**
     * 更新评价状态
     * @param orderId 订单ID
     * @param productId 商品ID
     * @return 操作是否成功
     * @implNote 将指定订单项标记为已评价
     */
    boolean updateCommentStatus(Long orderId, Long productId);

    /**
     * 根据订单ID查询订单项（返回实体）
     * @param orderId 订单ID
     * @return 订单项列表（包含商品详情）
     * @implNote 用于订单详情页展示
     */
    List<OrderItem> listByOrderIdEntities(Long orderId);
}
