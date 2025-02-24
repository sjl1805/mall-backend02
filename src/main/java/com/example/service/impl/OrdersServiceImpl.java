package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrdersMapper;
import com.example.model.entity.Orders;
import com.example.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【orders(订单表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:10
 */
@Service
@CacheConfig(cacheNames = "orders")
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    @Cacheable(value = "orders", key = "#userId")
    public List<Orders> selectByUserId(Long userId) {
        return ordersMapper.selectByUserId(userId);
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public Orders selectById(Long id) {
        return ordersMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "orders", key = "#orders.id")
    public boolean insertOrder(Orders orders) {
        return ordersMapper.insert(orders) > 0;
    }

    @Override
    @CacheEvict(value = "orders", key = "#orders.id")
    public boolean updateOrder(Orders orders) {
        return ordersMapper.updateById(orders) > 0;
    }

    @Override
    @CacheEvict(value = "orders", key = "#id")
    public boolean deleteOrder(Long id) {
        return ordersMapper.deleteById(id) > 0;
    }

    @Override
    public IPage<Orders> selectPage(IPage<Orders> page) {
        return ordersMapper.selectPage(page);
    }
}




