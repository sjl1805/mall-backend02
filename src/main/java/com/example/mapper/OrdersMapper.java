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
}




