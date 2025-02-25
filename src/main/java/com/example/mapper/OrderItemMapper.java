package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.OrderItem;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单项Mapper接口
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    
    /**
     * 根据订单ID查询所有订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 根据订单ID列表批量查询订单项
     * @param orderIds 订单ID列表
     * @return 订单项列表
     */
    List<OrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);
    
    /**
     * 根据商品ID查询订单项
     * @param productId 商品ID
     * @return 订单项列表
     */
    List<OrderItem> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据SKU ID查询订单项
     * @param skuId SKU ID
     * @return 订单项列表
     */
    List<OrderItem> selectBySkuId(@Param("skuId") Long skuId);
    
    /**
     * 批量插入订单项
     * @param list 订单项列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<OrderItem> list);
    
    /**
     * 统计商品销售数量
     * @param productId 商品ID
     * @return 销售总数
     */
    Integer countSalesByProductId(@Param("productId") Long productId);
    
    /**
     * 统计商品销售金额
     * @param productId 商品ID
     * @return 销售总金额
     */
    BigDecimal sumAmountByProductId(@Param("productId") Long productId);
    
    /**
     * 查询热销商品，按销售数量排序
     * @param limit 返回数量限制
     * @return 热销商品统计，包含productId、totalQuantity、totalAmount
     */
    @MapKey("productId")
    Map<Long, Map<String, Object>> selectHotProducts(@Param("limit") Integer limit);
    
    /**
     * 查询指定时间段内的商品销售情况
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 返回数量限制
     * @return 商品销售统计
     */
    @MapKey("productId")
    List<Map<String, Object>> selectProductSalesByDateRange(
        @Param("startTime") LocalDateTime startTime, 
        @Param("endTime") LocalDateTime endTime,
        @Param("limit") Integer limit
    );
    
    /**
     * 分页查询商品的订单项
     * @param page 分页参数
     * @param productId 商品ID
     * @return 分页数据
     */
    IPage<OrderItem> selectPageByProductId(Page<OrderItem> page, @Param("productId") Long productId);
    
    /**
     * 统计用户购买的商品种类数
     * @param userId 用户ID
     * @return 商品种类数
     */
    Integer countProductTypesByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户最近购买的商品
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 最近购买的商品列表
     */
    List<OrderItem> selectRecentPurchasesByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 查询用户购买某商品的次数
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 购买次数
     */
    Integer countUserProductPurchases(@Param("userId") Long userId, @Param("productId") Long productId);
} 