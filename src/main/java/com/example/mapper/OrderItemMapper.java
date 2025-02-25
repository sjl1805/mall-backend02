package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * @author 31815
 * @description 针对表【order_item(订单商品表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:13
 * @Entity model.entity.OrderItem
 */
@Mapper
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

    /**
     * 批量插入订单商品
     *
     * @param orderItems 订单商品列表
     * @return 插入结果
     */
    int batchInsertOrderItems(@Param("orderItems") List<OrderItem> orderItems);

    /**
     * 根据订单ID批量删除订单商品
     *
     * @param orderId 订单ID
     * @return 删除结果
     */
    int deleteByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据商品ID查询订单商品
     *
     * @param productId 商品ID
     * @return 订单商品列表
     */
    List<OrderItem> selectByProductId(@Param("productId") Long productId);

    /**
     * 统计商品销量
     *
     * @param productId 商品ID
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 销量
     */
    int countProductSales(
            @Param("productId") Long productId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    /**
     * 查询热销商品
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 热销商品列表
     */
    List<Map<String, Object>> selectHotProducts(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("limit") Integer limit);

    /**
     * 查询订单项关联的商品详情
     *
     * @param orderId 订单ID
     * @return 订单项商品详情列表
     */
    List<Map<String, Object>> selectOrderItemsWithProductDetails(@Param("orderId") Long orderId);

    /**
     * 获取用户购买过的商品ID列表
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 商品ID列表
     */
    List<Long> getUserPurchasedProductIds(
            @Param("userId") Long userId, 
            @Param("limit") Integer limit);

    /**
     * 获取订单项状态汇总
     *
     * @param orderId 订单ID
     * @return 状态汇总
     */
    Map<String, Object> getOrderItemStatusSummary(@Param("orderId") Long orderId);

    /**
     * 更新订单项评价状态
     *
     * @param id 订单项ID
     * @param reviewStatus 评价状态
     * @return 更新结果
     */
    int updateReviewStatus(
            @Param("id") Long id, 
            @Param("reviewStatus") Integer reviewStatus);

    /**
     * 更新订单项退款状态
     *
     * @param id 订单项ID
     * @param refundStatus 退款状态
     * @return 更新结果
     */
    int updateRefundStatus(
            @Param("id") Long id, 
            @Param("refundStatus") Integer refundStatus);
}




