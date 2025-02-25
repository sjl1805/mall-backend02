package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Orders;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【orders(订单表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:10
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 根据用户ID查询订单
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Orders> selectByUserId(Long userId);

    /**
     * 分页查询订单
     *
     * @param page 分页信息
     * @return 订单列表
     */
    IPage<Orders> selectPage(IPage<Orders> page);

    /**
     * 根据ID查询订单
     *
     * @param id 订单ID
     * @return 订单信息
     */
    Orders selectById(Long id);

    /**
     * 新增订单
     *
     * @param orders 订单信息
     * @return 插入结果
     */
    boolean insertOrder(Orders orders);

    /**
     * 更新订单信息
     *
     * @param orders 订单信息
     * @return 更新结果
     */
    boolean updateOrder(Orders orders);

    /**
     * 根据ID删除订单
     *
     * @param id 订单ID
     * @return 删除结果
     */
    boolean deleteOrder(Long id);

    /**
     * 根据订单号查询订单
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    Orders selectByOrderNo(String orderNo);
    
    /**
     * 根据订单状态查询
     *
     * @param status 订单状态
     * @return 订单列表
     */
    List<Orders> selectByStatus(Integer status);
    
    /**
     * 查询用户特定状态的订单
     *
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    List<Orders> selectByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据时间范围查询订单
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    List<Orders> selectByTimeRange(Date startTime, Date endTime);
    
    /**
     * 获取订单统计数据
     *
     * @param userId 用户ID (可选)
     * @return 订单统计数据
     */
    List<Map<String, Object>> getOrderStatistics(Long userId);
    
    /**
     * 查询最近创建的订单
     *
     * @param limit 限制数量
     * @return 订单列表
     */
    List<Orders> selectRecentOrders(Integer limit);
    
    /**
     * 更新订单支付信息
     *
     * @param orderNo 订单号
     * @param paymentMethod 支付方式
     * @param paymentTime 支付时间
     * @return 更新结果
     */
    boolean updatePaymentInfo(String orderNo, Integer paymentMethod, Date paymentTime);
    
    /**
     * 更新订单物流信息
     *
     * @param orderNo 订单号
     * @param logisticsCompany 物流公司
     * @param trackingNumber 物流单号
     * @return 更新结果
     */
    boolean updateShippingInfo(String orderNo, String logisticsCompany, String trackingNumber);
    
    /**
     * 查询订单详情（包含订单项）
     *
     * @param orderNo 订单号
     * @return 订单详情
     */
    Map<String, Object> selectOrderDetail(String orderNo);
    
    /**
     * 取消订单
     *
     * @param orderNo 订单号
     * @param cancelReason 取消原因
     * @return 取消结果
     */
    boolean cancelOrder(String orderNo, String cancelReason);
    
    /**
     * 确认收货
     *
     * @param orderNo 订单号
     * @return 确认结果
     */
    boolean confirmReceive(String orderNo);
    
    /**
     * 申请退款
     *
     * @param orderNo 订单号
     * @param refundReason 退款原因
     * @return 申请结果
     */
    boolean applyRefund(String orderNo, String refundReason);
    
    /**
     * 完成订单
     *
     * @param orderNo 订单号
     * @return 完成结果
     */
    boolean completeOrder(String orderNo);
    
    /**
     * 获取订单超时未支付数量
     *
     * @param minutes 超时分钟数
     * @return 超时订单数量
     */
    int countTimeoutOrders(int minutes);
    
    /**
     * 自动取消超时未支付订单
     *
     * @param minutes 超时分钟数
     * @return 取消的订单数量
     */
    int autoCancelTimeoutOrders(int minutes);
}
