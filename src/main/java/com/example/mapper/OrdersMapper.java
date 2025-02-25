package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * @author 31815
 * @description 针对表【orders(订单表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:10
 * @Entity model.entity.Orders
 */
@Mapper
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

    /**
     * 根据订单状态查询
     * 
     * @param status 订单状态
     * @return 订单列表
     */
    List<Orders> selectByStatus(@Param("status") Integer status);

    /**
     * 查询用户特定状态的订单
     * 
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    List<Orders> selectByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") Integer status);

    /**
     * 根据时间范围查询订单
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    List<Orders> selectByTimeRange(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    /**
     * 获取订单统计数据
     * 
     * @param userId 用户ID (可选)
     * @return 订单统计数据
     */
    List<Map<String, Object>> getOrderStatistics(@Param("userId") Long userId);

    /**
     * 查询最近创建的订单
     * 
     * @param limit 限制数量
     * @return 订单列表
     */
    List<Orders> selectRecentOrders(@Param("limit") Integer limit);

    /**
     * 更新订单支付信息
     * 
     * @param orderNo 订单号
     * @param paymentMethod 支付方式
     * @param paymentTime 支付时间
     * @return 更新结果
     */
    int updatePaymentInfo(
            @Param("orderNo") String orderNo,
            @Param("paymentMethod") Integer paymentMethod,
            @Param("paymentTime") Date paymentTime);

    /**
     * 更新订单物流信息
     * 
     * @param orderNo 订单号
     * @param logisticsCompany 物流公司
     * @param trackingNumber 物流单号
     * @return 更新结果
     */
    int updateShippingInfo(
            @Param("orderNo") String orderNo,
            @Param("logisticsCompany") String logisticsCompany,
            @Param("trackingNumber") String trackingNumber);

    /**
     * 查询订单详情（包含订单项）
     *
     * @param orderNo 订单号
     * @return 订单详情
     */
    Map<String, Object> selectOrderDetail(@Param("orderNo") String orderNo);

    /**
     * 取消订单
     * 
     * @param orderNo 订单号
     * @param cancelReason 取消原因
     * @return 更新结果
     */
    int cancelOrder(@Param("orderNo") String orderNo, @Param("cancelReason") String cancelReason);
    
    /**
     * 确认收货
     * 
     * @param orderNo 订单号
     * @return 更新结果
     */
    int confirmReceive(@Param("orderNo") String orderNo);
    
    /**
     * 申请退款
     * 
     * @param orderNo 订单号
     * @param refundReason 退款原因
     * @return 更新结果
     */
    int applyRefund(
            @Param("orderNo") String orderNo, 
            @Param("refundReason") String refundReason);
    
    /**
     * 完成订单
     * 
     * @param orderNo 订单号
     * @return 更新结果
     */
    int completeOrder(@Param("orderNo") String orderNo);
    
    /**
     * 查询超时未支付订单
     * 
     * @param timeoutTime 超时时间点
     * @return 超时订单列表
     */
    List<Orders> selectTimeoutOrders(@Param("timeoutTime") Date timeoutTime);
    
    /**
     * 统计超时未支付订单数量
     * 
     * @param timeoutTime 超时时间点
     * @return 超时订单数量
     */
    int countTimeoutOrders(@Param("timeoutTime") Date timeoutTime);
    
    /**
     * 自动取消超时未支付订单
     * 
     * @param timeoutTime 超时时间点
     * @return 取消的订单数量
     */
    int autoCancelTimeoutOrders(@Param("timeoutTime") Date timeoutTime);
    
    /**
     * 根据用户ID查询各状态订单数量
     *
     * @param userId 用户ID
     * @return 各状态订单数量
     */
    List<Map<String, Object>> countOrdersByStatus(@Param("userId") Long userId);
    
    /**
     * 查询订单销售统计（按日期）
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 按日期统计的销售数据
     */
    List<Map<String, Object>> getOrderSalesByDate(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}




