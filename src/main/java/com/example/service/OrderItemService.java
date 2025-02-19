package com.example.service;

import com.example.model.entity.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
/**
* @author 31815
* @description 针对表【order_item(订单商品表)】的数据库操作Service
* @createDate 2025-02-18 23:44:21
*/
public interface OrderItemService extends IService<OrderItem> {
    List<OrderItem> listByOrderId(Long orderId);
    boolean updateCommentStatus(Long orderId, Long productId);

}
