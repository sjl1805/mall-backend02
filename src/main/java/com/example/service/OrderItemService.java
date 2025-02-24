package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.OrderItem;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【order_item(订单商品表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:13
 */
public interface OrderItemService extends IService<OrderItem> {

    /**
     * 根据订单ID查询订单商品
     * @param orderId 订单ID
     * @return 订单商品列表
     */
    List<OrderItem> selectByOrderId(Long orderId);

    /**
     * 分页查询订单商品
     * @param page 分页信息
     * @return 订单商品列表
     */
    IPage<OrderItem> selectPage(IPage<OrderItem> page);

    /**
     * 根据ID查询订单商品
     * @param id 订单商品ID
     * @return 订单商品信息
     */
    OrderItem selectById(Long id);

    /**
     * 新增订单商品
     * @param orderItem 订单商品信息
     * @return 插入结果
     */
    boolean insertOrderItem(OrderItem orderItem);

    /**
     * 更新订单商品信息
     * @param orderItem 订单商品信息
     * @return 更新结果
     */
    boolean updateOrderItem(OrderItem orderItem);

    /**
     * 根据ID删除订单商品
     * @param id 订单商品ID
     * @return 删除结果
     */
    boolean deleteOrderItem(Long id);
}
