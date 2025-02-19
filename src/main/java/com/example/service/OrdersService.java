package com.example.service;

import com.example.model.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.order.OrderCreateDTO;

/**
* @author 31815
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2025-02-18 23:44:18
*/
public interface OrdersService extends IService<Orders> {
    String createOrder(OrderCreateDTO orderDTO);
    boolean cancelOrder(String orderNo);
    boolean payOrder(String orderNo);
    Orders getOrderDetail(String orderNo);
    void autoCancelUnpaidOrders();
}
