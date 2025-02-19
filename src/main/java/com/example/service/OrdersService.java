package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.order.OrderCreateDTO;
import com.example.model.entity.Orders;

/**
 * 订单服务接口
 * 
 * @author 31815
 * @description 提供订单全生命周期管理功能，包含：
 *              1. 订单创建与支付
 *              2. 订单状态管理
 *              3. 自动取消未支付订单
 *              4. 订单详情查询
 * @createDate 2025-02-18 23:44:18
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 创建新订单（事务操作）
     * @param orderDTO 订单创建参数，包含：
     *                 - userId: 用户ID（必须）
     *                 - addressId: 收货地址ID（必须）
     *                 - items: 订单项列表（至少一项）
     * @return 生成的订单号
     * @throws com.example.exception.BusinessException 当参数校验失败时抛出
     */
    String createOrder(OrderCreateDTO orderDTO);

    /**
     * 取消订单（用户操作）
     * @param orderNo 订单号
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当订单不可取消时抛出
     */
    boolean cancelOrder(String orderNo);

    /**
     * 支付订单（模拟支付）
     * @param orderNo 订单号
     * @return 支付是否成功
     * @throws com.example.exception.BusinessException 当订单不可支付时抛出
     */
    boolean payOrder(String orderNo);

    /**
     * 获取订单详情（带订单项）
     * @param orderNo 订单号
     * @return 完整订单信息（包含订单项列表）
     * @implNote 结果缓存优化，有效期30分钟
     */
    Orders getOrderDetail(String orderNo);

    /**
     * 自动取消未支付订单（定时任务）
     * @implNote 每30分钟执行，取消超过30分钟未支付的订单
     */
    void autoCancelUnpaidOrders();
}
