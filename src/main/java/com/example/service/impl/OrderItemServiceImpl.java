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

import java.util.List;

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
}




