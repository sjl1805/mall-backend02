package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Orders;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 31815
 * @description 针对表【orders(订单表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:10
 * @Entity model.entity.Orders
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 创建订单
     * @param order 订单实体
     * @return 影响行数
     */
    int insertOrder(Orders order);

    /**
     * 更新支付状态
     * @param orderNo 订单编号
     * @param status 新状态
     * @param paymentTime 支付时间
     * @param payAmount 实际支付金额
     * @return 影响行数
     */
    int updatePaymentStatus(@Param("orderNo") String orderNo, 
                           @Param("status") Integer status,
                           @Param("paymentTime") Date paymentTime,
                           @Param("payAmount") BigDecimal payAmount);

    /**
     * 更新物流信息
     * @param orderId 订单ID
     * @param logisticsCompany 物流公司
     * @param trackingNumber 运单号
     * @return 影响行数
     */
    int updateLogisticsInfo(@Param("orderId") Long orderId,
                           @Param("logisticsCompany") String logisticsCompany,
                           @Param("trackingNumber") String trackingNumber);

    /**
     * 订单状态流转
     * @param orderId 订单ID
     * @param oldStatus 原状态
     * @param newStatus 新状态
     * @return 影响行数
     */
    int updateOrderStatus(@Param("orderId") Long orderId,
                         @Param("oldStatus") Integer oldStatus,
                         @Param("newStatus") Integer newStatus);

    /**
     * 根据订单号查询
     * @param orderNo 订单编号
     * @return 订单实体
     */
    Orders selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 分页查询订单
     * @param page 分页参数
     * @param userId 用户ID
     * @param query 查询条件
     * @return 分页结果
     */
    Page<Orders> selectOrdersPage(Page<Orders> page, 
                                  @Param("userId") Long userId,
                                  @Param("query") OrderQuery query);

    // 查询条件封装类
    @Data
    class OrderQuery {
        private Integer status;
        private Date startTime;
        private Date endTime;
    }
}




