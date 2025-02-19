package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrdersMapper;
import com.example.model.dto.order.OrderCreateDTO;
import com.example.model.entity.OrderItem;
import com.example.model.entity.Orders;
import com.example.service.OrderItemService;
import com.example.service.OrdersService;
import com.example.utils.OrderNoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 31815
 * @description 针对表【orders(订单表)】的数据库操作Service实现
 * @createDate 2025-02-18 23:44:18
 */
@Service
@CacheConfig(cacheNames = "orderService")
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {

    private final OrderItemService orderItemService;

    public OrdersServiceImpl(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public String createOrder(OrderCreateDTO orderDTO) {
        // 生成订单号
        String orderNo = OrderNoUtils.generate("PC");

        // 创建订单主表
        Orders order = new Orders();
        BeanUtils.copyProperties(orderDTO, order);
        order.setOrderNo(orderNo);
        order.setStatus(0); // 待支付状态
        save(order);

        // 创建订单项
        List<OrderItem> items = orderDTO.getItems().stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            BeanUtils.copyProperties(itemDTO, item);
            item.setOrderId(order.getId());
            return item;
        }).collect(Collectors.toList());

        orderItemService.saveBatch(items);

        return orderNo;
    }

    @Override
    @Transactional
    @CacheEvict(key = "#orderNo")
    public boolean cancelOrder(String orderNo) {
        return lambdaUpdate()
                .eq(Orders::getOrderNo, orderNo)
                .set(Orders::getStatus, 4) // 已取消
                .update();
    }

    @Override
    @Transactional
    @CacheEvict(key = "#orderNo")
    public boolean payOrder(String orderNo) {
        return lambdaUpdate()
                .eq(Orders::getOrderNo, orderNo)
                .set(Orders::getStatus, 1) // 已支付
                .set(Orders::getPaymentTime, new Date())
                .update();
    }

    @Override
    @Cacheable(key = "'order:' + #orderNo")
    public Orders getOrderDetail(String orderNo) {
        Orders order = lambdaQuery()
                .eq(Orders::getOrderNo, orderNo)
                .one();
        if (order != null) {
            order.setItems(orderItemService.listByOrderId(order.getId()));
        }
        return order;
    }

    @Override
    @Scheduled(cron = "0 0/30 * * * ?") // 每30分钟执行一次
    @Transactional
    @CacheEvict(allEntries = true)
    public void autoCancelUnpaidOrders() {
        baseMapper.autoCancelOrders(30); // 30分钟未支付自动取消
    }
}




