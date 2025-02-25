package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrdersMapper;
import com.example.model.entity.Orders;
import com.example.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单服务实现类
 * 
 * 该类实现了订单相关的业务逻辑，包括订单的创建、查询、更新和删除等核心功能。
 * 订单是电商系统中最核心的业务实体，连接了用户、商品、支付、物流等多个业务领域。
 * 订单状态变更需要严格的事务管理，确保数据一致性和业务完整性。
 * 使用了Spring缓存机制对订单信息进行缓存，提高查询效率，减轻数据库压力。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【orders(订单表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:10
 */
@Service
@CacheConfig(cacheNames = "orders") // 指定该服务类的缓存名称为"orders"
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 根据用户ID查询订单列表
     * 
     * 该方法从缓存或数据库获取指定用户的所有订单信息，
     * 用于个人中心的订单列表展示，让用户了解自己的订单历史和状态
     *
     * @param userId 用户ID
     * @return 用户订单列表
     */
    @Override
    @Cacheable(value = "orders", key = "#userId") // 缓存用户订单数据，提高查询效率
    public List<Orders> selectByUserId(Long userId) {
        return ordersMapper.selectByUserId(userId);
    }

    /**
     * 根据ID查询订单详情
     * 
     * 该方法从缓存或数据库获取指定ID的订单详情，
     * 用于订单详情页展示和后台订单管理
     *
     * @param id 订单ID
     * @return 订单实体
     */
    @Override
    @Cacheable(value = "orders", key = "#id") // 缓存订单详情，提高查询效率
    public Orders selectById(Long id) {
        return ordersMapper.selectById(id);
    }

    /**
     * 创建订单
     * 
     * 该方法用于用户下单时创建新订单，
     * 是电商交易流程的起点，可能涉及库存锁定、优惠券使用等操作，
     * 需要在事务中执行以确保数据一致性
     *
     * @param orders 订单实体
     * @return 创建成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#orders.id") // 清除订单缓存
    public boolean insertOrder(Orders orders) {
        return ordersMapper.insert(orders) > 0;
    }

    /**
     * 更新订单
     * 
     * 该方法用于更新订单状态或信息，如支付成功、发货、确认收货等状态变更，
     * 是订单生命周期管理的核心方法，需要在事务中执行以确保数据一致性
     *
     * @param orders 订单实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#orders.id") // 清除订单缓存
    public boolean updateOrder(Orders orders) {
        return ordersMapper.updateById(orders) > 0;
    }

    /**
     * 删除订单
     * 
     * 该方法用于用户删除订单或后台管理系统清理订单，
     * 实际业务中通常建议使用软删除（修改状态）而非物理删除，
     * 以保留交易历史记录，并需要在事务中执行
     *
     * @param id 订单ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#id") // 清除被删除订单的缓存
    public boolean deleteOrder(Long id) {
        return ordersMapper.deleteById(id) > 0;
    }

    /**
     * 分页查询订单数据
     * 
     * 该方法用于后台管理系统分页查看订单数据，
     * 支持按时间、金额、状态等条件查询和排序，
     * 是订单管理的基础功能
     *
     * @param page 分页参数
     * @return 订单分页数据
     */
    @Override
    public IPage<Orders> selectPage(IPage<Orders> page) {
        return ordersMapper.selectPage(page);
    }
}




