package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrdersMapper;
import com.example.model.entity.Orders;
import com.example.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【orders(订单表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:10
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public List<Orders> selectByUserId(Long userId) {
        return ordersMapper.selectByUserId(userId);
    }

    @Override
    public IPage<Orders> selectPage(IPage<Orders> page) {
        return ordersMapper.selectPage(page);
    }

    @Override
    public Orders selectById(Long id) {
        return ordersMapper.selectById(id);
    }

    @Override
    public boolean insertOrder(Orders orders) {
        return ordersMapper.insert(orders) > 0;
    }

    @Override
    public boolean updateOrder(Orders orders) {
        return ordersMapper.updateById(orders) > 0;
    }

    @Override
    public boolean deleteOrder(Long id) {
        return ordersMapper.deleteById(id) > 0;
    }
}




