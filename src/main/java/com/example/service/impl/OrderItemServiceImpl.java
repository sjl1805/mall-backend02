package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrderItemMapper;
import com.example.model.entity.OrderItem;
import com.example.service.OrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单项服务实现类
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return baseMapper.findByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchOrderItems(List<OrderItem> orderItems) {
        return saveBatch(orderItems);
    }

    @Override
    public int countProductSales(Long productId) {
        Integer result = baseMapper.countSalesByProductId(productId);
        return result == null ? 0 : result;
    }
} 