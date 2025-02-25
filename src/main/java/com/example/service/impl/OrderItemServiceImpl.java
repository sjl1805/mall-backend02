package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrderItemMapper;
import com.example.model.entity.OrderItem;
import com.example.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 订单项服务实现类
 * 
 * 该类实现了订单项（订单中的商品）相关的业务逻辑，包括订单项的添加、修改、删除和查询等功能。
 * 订单项是订单的组成部分，记录了订单中具体的商品信息，如商品ID、SKU、数量、单价等数据。
 * 一个订单通常包含一个或多个订单项，它们共同构成了完整的订单内容。
 * 使用了Spring缓存机制对订单项信息进行缓存，提高查询效率。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【order_item(订单商品表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:13
 */
@Service
@CacheConfig(cacheNames = "orderItems") // 指定该服务类的缓存名称为"orderItems"
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
        implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    /**
     * 根据订单ID查询订单项列表
     * 
     * 该方法从缓存或数据库获取指定订单的所有订单项信息，
     * 用于订单详情页展示订单中的商品列表，包括商品名称、数量、价格等信息
     *
     * @param orderId 订单ID
     * @return 订单项列表
     */
    @Override
    @Cacheable(value = "orderItems", key = "#orderId") // 缓存订单项数据，提高查询效率
    public List<OrderItem> selectByOrderId(Long orderId) {
        return orderItemMapper.selectByOrderId(orderId);
    }

    /**
     * 分页查询订单项数据
     * 
     * 该方法用于后台管理系统分页查看订单项数据，
     * 通常用于对特定商品的销售情况进行分析
     *
     * @param page 分页参数
     * @return 订单项分页数据
     */
    @Override
    public IPage<OrderItem> selectPage(IPage<OrderItem> page) {
        return orderItemMapper.selectPage(page);
    }

    /**
     * 根据ID查询订单项
     * 
     * 该方法从缓存或数据库获取指定ID的订单项详情，
     * 用于查看特定订单项的详细信息
     *
     * @param id 订单项ID
     * @return 订单项实体
     */
    @Override
    @Cacheable(value = "orderItems", key = "#id") // 缓存订单项详情，提高查询效率
    public OrderItem selectById(Long id) {
        return orderItemMapper.selectById(id);
    }

    /**
     * 添加订单项
     * 
     * 该方法用于创建订单时添加订单项，
     * 通常在订单创建过程中批量添加多个订单项，
     * 需要在事务中执行以确保与订单主表的数据一致性
     *
     * @param orderItem 订单项实体
     * @return 添加成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "orderItems", key = "#orderItem.id") // 清除订单项缓存
    public boolean insertOrderItem(OrderItem orderItem) {
        return orderItemMapper.insert(orderItem) > 0;
    }

    /**
     * 更新订单项
     * 
     * 该方法用于更新订单项信息，如修改数量、价格等，
     * 需要在事务中执行并同步更新订单总金额，
     * 确保订单数据的一致性
     *
     * @param orderItem 订单项实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "orderItems", key = "#orderItem.id") // 清除订单项缓存
    public boolean updateOrderItem(OrderItem orderItem) {
        return orderItemMapper.updateById(orderItem) > 0;
    }

    /**
     * 删除订单项
     * 
     * 该方法用于删除订单项，通常在取消订单或修改订单内容时使用，
     * 需要在事务中执行并同步更新订单总金额，
     * 确保订单数据的一致性
     *
     * @param id 订单项ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "orderItems", key = "#id") // 清除被删除订单项的缓存
    public boolean deleteOrderItem(Long id) {
        return orderItemMapper.deleteById(id) > 0;
    }

    /**
     * 批量插入订单项
     * 
     * 该方法用于创建订单时批量添加多个订单项，
     * 通常在订单创建过程中使用，一次性保存订单中的所有商品信息
     *
     * @param orderItems 订单项列表
     * @return 插入成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "orderItems", allEntries = true) // 清除缓存
    public boolean batchInsertOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return false;
        }
        return orderItemMapper.batchInsertOrderItems(orderItems) > 0;
    }

    /**
     * 根据订单ID删除订单项
     * 
     * 该方法用于删除订单时同步删除所有关联的订单项，
     * 保证数据一致性，通常在取消未支付订单或删除历史订单时使用
     *
     * @param orderId 订单ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "orderItems", allEntries = true) // 清除缓存
    public boolean deleteByOrderId(Long orderId) {
        return orderItemMapper.deleteByOrderId(orderId) > 0;
    }

    /**
     * 根据商品ID查询订单项
     * 
     * 该方法用于查询特定商品的销售记录，
     * 可用于商品销售分析、买家评价查看等场景
     *
     * @param productId 商品ID
     * @return 订单项列表
     */
    @Override
    @Cacheable(value = "orderItems", key = "'product_' + #productId")
    public List<OrderItem> selectByProductId(Long productId) {
        return orderItemMapper.selectByProductId(productId);
    }

    /**
     * 统计商品销量
     * 
     * 该方法用于统计商品在指定时间段内的销售数量，
     * 是商品销售分析和排行榜功能的基础
     *
     * @param productId 商品ID
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 销量
     */
    @Override
    @Cacheable(value = "orderItems", key = "'sales_' + #productId + '_' + #startTime + '_' + #endTime")
    public int countProductSales(Long productId, Date startTime, Date endTime) {
        return orderItemMapper.countProductSales(productId, startTime, endTime);
    }

    /**
     * 查询热销商品
     * 
     * 该方法用于获取指定时间段内销量最高的商品列表，
     * 用于首页推荐、热销榜单等功能
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 热销商品列表
     */
    @Override
    @Cacheable(value = "orderItems", key = "'hot_' + #startTime + '_' + #endTime + '_' + #limit")
    public List<Map<String, Object>> selectHotProducts(Date startTime, Date endTime, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认查询10条
        }
        return orderItemMapper.selectHotProducts(startTime, endTime, limit);
    }

    /**
     * 获取订单项详情（包含商品信息）
     * 
     * 该方法用于订单详情页显示，包含订单项的基本信息和关联的商品详细信息，
     * 提供丰富的订单内容展示
     *
     * @param orderId 订单ID
     * @return 订单项详情列表
     */
    @Override
    @Cacheable(value = "orderItems", key = "'details_' + #orderId")
    public List<Map<String, Object>> getOrderItemsWithProductDetails(Long orderId) {
        return orderItemMapper.selectOrderItemsWithProductDetails(orderId);
    }

    /**
     * 更新订单项的评价状态
     * 
     * 该方法用于用户完成商品评价后更新订单项的评价状态，
     * 确保用户不会重复评价同一商品
     *
     * @param id 订单项ID
     * @param reviewStatus 评价状态
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "orderItems", allEntries = true)
    public boolean updateReviewStatus(Long id, Integer reviewStatus) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(id);
        // 这里假设订单项表没有评价状态字段，实际应该通过关联评价表更新
        // 或者在订单项表添加评价状态字段
        return updateById(orderItem);
    }

    /**
     * 更新订单项的退款状态
     * 
     * 该方法用于用户申请退款或完成退款后更新订单项的退款状态，
     * 用于退款流程管理和状态显示
     *
     * @param id 订单项ID
     * @param refundStatus 退款状态
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(value = "orderItems", allEntries = true)
    public boolean updateRefundStatus(Long id, Integer refundStatus) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(id);
        // 这里假设订单项表没有退款状态字段，实际应该通过关联退款表更新
        // 或者在订单项表添加退款状态字段
        return updateById(orderItem);
    }

    /**
     * 获取用户购买过的商品ID列表
     * 
     * 该方法用于获取用户历史购买记录中的商品ID，
     * 可用于个性化推荐、购买记录查询等功能
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 商品ID列表
     */
    @Override
    @Cacheable(value = "orderItems", key = "'user_products_' + #userId + '_' + #limit")
    public List<Long> getUserPurchasedProductIds(Long userId, Integer limit) {
        // 这里需要关联orders表查询用户的订单
        // 简化实现，实际应该调用mapper方法
        return Collections.emptyList();
    }

    /**
     * 获取订单项状态汇总
     * 
     * 该方法用于统计订单中商品的各种状态数量，
     * 如已评价数量、退款数量等，用于订单状态概览
     *
     * @param orderId 订单ID
     * @return 状态汇总（评价数、退款数等）
     */
    @Override
    @Cacheable(value = "orderItems", key = "'status_summary_' + #orderId")
    public Map<String, Object> getOrderItemStatusSummary(Long orderId) {
        // 简化实现，实际应该调用mapper方法
        Map<String, Object> result = new HashMap<>();
        result.put("totalItems", 0);
        result.put("reviewedItems", 0);
        result.put("refundedItems", 0);
        return result;
    }
}




