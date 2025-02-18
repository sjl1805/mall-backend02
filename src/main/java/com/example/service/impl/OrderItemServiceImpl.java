package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.OrderItemService;
import com.example.model.entity.OrderItem;
import com.example.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【order_item(订单商品表)】的数据库操作Service实现
* @createDate 2025-02-18 23:44:21
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService {

}




