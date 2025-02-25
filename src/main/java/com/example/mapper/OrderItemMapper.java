package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单项数据访问层接口
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    
    /**
     * 根据订单ID查询订单项列表
     *
     * @param orderId 订单ID
     * @return 订单项列表
     */
    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 统计商品的销售数量
     *
     * @param productId 商品ID
     * @return 销售数量
     */
    @Select("SELECT SUM(quantity) FROM order_item oi " +
            "JOIN orders o ON oi.order_id = o.id " +
            "WHERE oi.product_id = #{productId} AND o.status > 0")
    Integer countSalesByProductId(@Param("productId") Long productId);
} 