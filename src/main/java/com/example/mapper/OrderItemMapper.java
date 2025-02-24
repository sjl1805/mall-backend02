package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【order_item(订单商品表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:13
 * @Entity model.entity.OrderItem
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据订单ID查询详情
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 批量插入订单项
     * @param items 订单项列表
     * @return 影响行数
     */
    int insertBatch(@Param("items") List<OrderItem> items);

    /**
     * 批量更新订单项状态
     * @param ids 订单项ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatusBatch(@Param("ids") List<Long> ids, 
                         @Param("status") Integer status);

    /**
     * 关联订单信息查询
     * @param itemId 订单项ID
     * @return 包含订单项和订单信息的Map
     */
    Map<String, Object> selectWithOrder(@Param("itemId") Long itemId);

}




