package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Orders;
import com.example.model.query.OrderQuery;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;


/**
* @author 31815
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2025-02-18 23:44:18
* @Entity model.entity.Orders
*/
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 分页查询订单列表（带条件）
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<Orders> selectOrderPage(IPage<Orders> page, @Param("query") OrderQuery query);

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatus(@Param("orderId") Long orderId, @Param("status") Integer status);

    /**
     * 自动取消超时订单
     * @param timeoutMinutes 超时时间（分钟）
     * @return 取消数量
     */
    int autoCancelOrders(@Param("timeoutMinutes") Integer timeoutMinutes);

    /**
     * 统计订单金额
     * @param query 统计条件
     * @return 统计结果
     */
    BigDecimal sumOrderAmount(@Param("query") OrderQuery query);
}




