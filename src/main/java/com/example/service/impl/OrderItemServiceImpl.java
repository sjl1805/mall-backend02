package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrderItemMapper;
import com.example.model.entity.OrderItem;
import com.example.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【order_item(订单商品表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:13
 */
@Service
@CacheConfig(cacheNames = "orderItems")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
        implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Cacheable(value = "orderItems", key = "#orderId")
    public List<OrderItem> selectByOrderId(Long orderId) {
        return orderItemMapper.selectByOrderId(orderId);
    }

    @Override
    public IPage<OrderItem> selectPage(IPage<OrderItem> page) {
        return orderItemMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "orderItems", key = "#id")
    public OrderItem selectById(Long id) {
        return orderItemMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "orderItems", key = "#orderItem.id")
    public boolean insertOrderItem(OrderItem orderItem) {
        return orderItemMapper.insert(orderItem) > 0;
    }

    @Override
    @CacheEvict(value = "orderItems", key = "#orderItem.id")
    public boolean updateOrderItem(OrderItem orderItem) {
        return orderItemMapper.updateById(orderItem) > 0;
    }

    @Override
    @CacheEvict(value = "orderItems", key = "#id")
    public boolean deleteOrderItem(Long id) {
        return orderItemMapper.deleteById(id) > 0;
    }
}




