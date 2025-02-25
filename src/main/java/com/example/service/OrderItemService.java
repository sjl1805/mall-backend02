package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.OrderItem;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【order_item(订单商品表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:13
 */
public interface OrderItemService extends IService<OrderItem> {

    /**
     * 根据订单ID查询订单商品
     *
     * @param orderId 订单ID
     * @return 订单商品列表
     */
    List<OrderItem> selectByOrderId(Long orderId);

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
    OrderItem selectById(Long id);

    /**
     * 新增订单商品
     *
     * @param orderItem 订单商品信息
     * @return 插入结果
     */
    boolean insertOrderItem(OrderItem orderItem);

    /**
     * 更新订单商品信息
     *
     * @param orderItem 订单商品信息
     * @return 更新结果
     */
    boolean updateOrderItem(OrderItem orderItem);

    /**
     * 根据ID删除订单商品
     *
     * @param id 订单商品ID
     * @return 删除结果
     */
    boolean deleteOrderItem(Long id);

    /**
     * 批量插入订单项
     *
     * @param orderItems 订单项列表
     * @return 插入结果
     */
    boolean batchInsertOrderItems(List<OrderItem> orderItems);

    /**
     * 根据订单ID删除订单项
     *
     * @param orderId 订单ID
     * @return 删除结果
     */
    boolean deleteByOrderId(Long orderId);

    /**
     * 根据商品ID查询订单项
     *
     * @param productId 商品ID
     * @return 订单项列表
     */
    List<OrderItem> selectByProductId(Long productId);

    /**
     * 统计商品销量
     *
     * @param productId 商品ID
     * @param startTime 开始时间（可选）
     * @param endTime   结束时间（可选）
     * @return 销量
     */
    int countProductSales(Long productId, Date startTime, Date endTime);

    /**
     * 查询热销商品
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param limit     限制数量
     * @return 热销商品列表
     */
    List<Map<String, Object>> selectHotProducts(Date startTime, Date endTime, Integer limit);

    /**
     * 获取订单项详情（包含商品信息）
     *
     * @param orderId 订单ID
     * @return 订单项详情列表
     */
    List<Map<String, Object>> getOrderItemsWithProductDetails(Long orderId);

    /**
     * 更新订单项的评价状态
     *
     * @param id           订单项ID
     * @param reviewStatus 评价状态
     * @return 更新结果
     */
    boolean updateReviewStatus(Long id, Integer reviewStatus);

    /**
     * 更新订单项的退款状态
     *
     * @param id           订单项ID
     * @param refundStatus 退款状态
     * @return 更新结果
     */
    boolean updateRefundStatus(Long id, Integer refundStatus);

    /**
     * 获取用户购买过的商品ID列表
     *
     * @param userId 用户ID
     * @param limit  限制数量
     * @return 商品ID列表
     */
    List<Long> getUserPurchasedProductIds(Long userId, Integer limit);

    /**
     * 获取订单项状态汇总
     *
     * @param orderId 订单ID
     * @return 状态汇总（评价数、退款数等）
     */
    Map<String, Object> getOrderItemStatusSummary(Long orderId);
}
