package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.entity.Orders;
import com.example.service.OrdersService;
import com.example.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2025-02-24 12:04:10
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




