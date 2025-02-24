package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrderItemMapper;
import com.example.model.entity.OrderItem;
import com.example.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【order_item(订单商品表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:13
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
        implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> selectByOrderId(Long orderId) {
        return orderItemMapper.selectByOrderId(orderId);
    }

    @Override
    public IPage<OrderItem> selectPage(IPage<OrderItem> page) {
        return orderItemMapper.selectPage(page);
    }

    @Override
    public OrderItem selectById(Long id) {
        return orderItemMapper.selectById(id);
    }

    @Override
    public boolean insertOrderItem(OrderItem orderItem) {
        return orderItemMapper.insert(orderItem) > 0;
    }

    @Override
    public boolean updateOrderItem(OrderItem orderItem) {
        return orderItemMapper.updateById(orderItem) > 0;
    }

    @Override
    public boolean deleteOrderItem(Long id) {
        return orderItemMapper.deleteById(id) > 0;
    }
}




