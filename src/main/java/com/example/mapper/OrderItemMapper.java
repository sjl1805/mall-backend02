package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【order_item(订单商品表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:13
 * @Entity model.entity.OrderItem
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据订单ID查询订单商品
     *
     * @param orderId 订单ID
     * @return 订单商品列表
     */
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 分页查询订单商品
     *
     * @param page 分页信息
     * @return 订单商品列表
     */
    IPage<OrderItem> selectPage(IPage<OrderItem> page);

    /**
     * 根据ID查询订单商品
     *
     * @param id 订单商品ID
     * @return 订单商品信息
     */
    OrderItem selectById(@Param("id") Long id);

    /**
     * 插入新订单商品
     *
     * @param orderItem 订单商品信息
     * @return 插入结果
     */
    int insertOrderItem(OrderItem orderItem);

    /**
     * 更新订单商品信息
     *
     * @param orderItem 订单商品信息
     * @return 更新结果
     */
    int updateOrderItem(OrderItem orderItem);

    /**
     * 根据ID删除订单商品
     *
     * @param id 订单商品ID
     * @return 删除结果
     */
    int deleteOrderItem(@Param("id") Long id);
}




