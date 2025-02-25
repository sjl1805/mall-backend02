package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单商品服务接口
 */
public interface OrderItemService extends IService<OrderItem> {
    
    /**
     * 根据订单ID查询所有订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> getByOrderId(Long orderId);
    
    /**
     * 根据订单ID列表批量查询订单项
     * @param orderIds 订单ID列表
     * @return 订单项列表
     */
    List<OrderItem> getByOrderIds(List<Long> orderIds);
    
    /**
     * 根据商品ID查询订单项
     * @param productId 商品ID
     * @return 订单项列表
     */
    List<OrderItem> getByProductId(Long productId);
    
    /**
     * 根据SKU ID查询订单项
     * @param skuId SKU ID
     * @return 订单项列表
     */
    List<OrderItem> getBySkuId(Long skuId);
    
    /**
     * 批量保存订单项
     * @param orderItems 订单项列表
     * @return 是否保存成功
     */
    boolean batchSaveOrderItems(List<OrderItem> orderItems);
    
    /**
     * 统计商品销售数量
     * @param productId 商品ID
     * @return 销售总数
     */
    Integer countProductSales(Long productId);
    
    /**
     * 统计商品销售金额
     * @param productId 商品ID
     * @return 销售总金额
     */
    BigDecimal sumProductAmount(Long productId);
    
    /**
     * 查询热销商品，按销售数量排序
     * @param limit 返回数量限制
     * @return 热销商品统计，包含productId、totalQuantity、totalAmount、productName
     */
    Map<Long, Map<String, Object>> getHotProducts(Integer limit);
    
    /**
     * 查询指定时间段内的商品销售情况
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 返回数量限制
     * @return 商品销售统计
     */
    List<Map<String, Object>> getProductSalesByDateRange(
        LocalDateTime startTime, 
        LocalDateTime endTime,
        Integer limit
    );
    
    /**
     * 分页查询商品的订单项
     * @param productId 商品ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<OrderItem> getProductOrderItemsPage(Long productId, Page<OrderItem> page);
    
    /**
     * 统计用户购买的商品种类数
     * @param userId 用户ID
     * @return 商品种类数
     */
    Integer countUserProductTypes(Long userId);
    
    /**
     * 查询用户最近购买的商品
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 最近购买的商品列表
     */
    List<OrderItem> getUserRecentPurchases(Long userId, Integer limit);
    
    /**
     * 查询用户购买某商品的次数
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 购买次数
     */
    Integer countUserProductPurchases(Long userId, Long productId);
    
    /**
     * 更新订单项的商品信息
     * @param orderItem 订单项信息
     * @return 是否更新成功
     */
    boolean updateOrderItemInfo(OrderItem orderItem);
    
    /**
     * 计算订单项总金额
     * @param price 单价
     * @param quantity 数量
     * @return 总金额
     */
    BigDecimal calculateItemAmount(BigDecimal price, Integer quantity);
} 