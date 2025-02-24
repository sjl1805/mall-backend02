package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【orders(订单表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:10
 * @Entity model.entity.Orders
 */
public interface OrdersMapper extends BaseMapper<Orders> {
    // 根据用户ID查询订单
    List<Orders> selectByUserId(@Param("userId") Long userId);

    // 分页查询订单
    IPage<Orders> selectPage(IPage<Orders> page);

    // 根据订单号查询
    Orders selectByOrderNo(@Param("orderNo") String orderNo);

    // 新增订单
    int insertOrder(Orders order);

    // 更新订单状态
    int updateOrderStatus(@Param("orderNo") String orderNo,
                          @Param("status") Integer status);

    // 删除订单
    int deleteOrder(@Param("id") Long id);
}




