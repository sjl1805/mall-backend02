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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单服务实现类
 * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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

    /**
     * 根据订单号查询订单
     * <p>
     * 该方法用于通过订单编号查询订单详情，
     * 适用于订单支付回调、物流查询等场景
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    @Override
    @Cacheable(key = "'no_' + #orderNo")
    public Orders selectByOrderNo(String orderNo) {
        return ordersMapper.selectByOrderNo(orderNo);
    }

    /**
     * 根据订单状态查询
     * <p>
     * 该方法用于查询特定状态的所有订单，
     * 主要用于后台管理系统的订单处理，如查询待发货订单等
     *
     * @param status 订单状态
     * @return 订单列表
     */
    @Override
    public List<Orders> selectByStatus(Integer status) {
        return ordersMapper.selectByStatus(status);
    }

    /**
     * 查询用户特定状态的订单
     * <p>
     * 该方法查询用户的特定状态订单，
     * 用于前台用户订单中心按状态筛选订单
     *
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    @Override
    @Cacheable(key = "#userId + '_status_' + #status")
    public List<Orders> selectByUserIdAndStatus(Long userId, Integer status) {
        return ordersMapper.selectByUserIdAndStatus(userId, status);
    }

    /**
     * 根据时间范围查询订单
     * <p>
     * 该方法用于按时间段查询订单数据，
     * 适用于订单统计分析、销售报表生成等场景
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单列表
     */
    @Override
    public List<Orders> selectByTimeRange(Date startTime, Date endTime) {
        return ordersMapper.selectByTimeRange(startTime, endTime);
    }

    /**
     * 获取订单统计数据
     *
     * @param userId 用户ID (可选)
     * @return 订单统计数据
     */
    @Override
    public List<Map<String, Object>> getOrderStatistics(Long userId) {
        return ordersMapper.getOrderStatistics(userId);
    }

    /**
     * 查询最近创建的订单
     *
     * @param limit 限制数量
     * @return 订单列表
     */
    @Override
    public List<Orders> selectRecentOrders(Integer limit) {
        return ordersMapper.selectRecentOrders(limit);
    }

    /**
     * 更新订单支付信息
     * <p>
     * 该方法在用户完成支付后更新订单的支付状态和支付方式，
     * 通常由支付回调接口调用，更新后可能触发订单发货流程
     *
     * @param orderNo       订单号
     * @param paymentMethod 支付方式
     * @param paymentTime   支付时间
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(key = "'no_' + #orderNo")
    public boolean updatePaymentInfo(String orderNo, Integer paymentMethod, Date paymentTime) {
        return ordersMapper.updatePaymentInfo(orderNo, paymentMethod, paymentTime) > 0;
    }

    /**
     * 更新订单物流信息
     * <p>
     * 该方法在商品发货后更新订单的物流信息，
     * 包括物流公司和运单号，便于用户查询物流状态
     *
     * @param orderNo          订单号
     * @param logisticsCompany 物流公司
     * @param trackingNumber   物流单号
     * @return 更新结果
     */
    @Override
    @Transactional
    @CacheEvict(key = "'no_' + #orderNo")
    public boolean updateShippingInfo(String orderNo, String logisticsCompany, String trackingNumber) {
        return ordersMapper.updateShippingInfo(orderNo, logisticsCompany, trackingNumber) > 0;
    }

    /**
     * 查询订单详情（包含订单项）
     * <p>
     * 该方法查询订单的完整信息，包括订单基本信息和订单项列表，
     * 用于订单详情页展示和后台订单管理
     *
     * @param orderNo 订单号
     * @return 订单详情
     */
    @Override
    @Cacheable(key = "'detail_' + #orderNo")
    public Map<String, Object> selectOrderDetail(String orderNo) {
        return ordersMapper.selectOrderDetail(orderNo);
    }

    /**
     * 取消订单
     * <p>
     * 该方法用于用户取消未支付订单或后台管理员取消异常订单，
     * 可能需要释放库存、恢复优惠券等操作，确保数据一致性
     *
     * @param orderNo      订单号
     * @param cancelReason 取消原因
     * @return 取消结果
     */
    @Override
    @Transactional
    @CacheEvict(key = "'no_' + #orderNo")
    public boolean cancelOrder(String orderNo, String cancelReason) {
        // 这里可以添加释放库存、恢复优惠券等逻辑
        return ordersMapper.cancelOrder(orderNo, cancelReason) > 0;
    }

    /**
     * 确认收货
     * <p>
     * 该方法用于用户确认已收到商品，
     * 将订单状态更新为已完成，可能触发佣金结算、积分发放等后续流程
     *
     * @param orderNo 订单号
     * @return 确认结果
     */
    @Override
    @Transactional
    @CacheEvict(key = "'no_' + #orderNo")
    public boolean confirmReceive(String orderNo) {
        return ordersMapper.confirmReceive(orderNo) > 0;
    }

    /**
     * 申请退款
     * <p>
     * 该方法用于用户申请订单退款，
     * 将订单状态更新为退款中，等待商家或平台处理
     *
     * @param orderNo      订单号
     * @param refundReason 退款原因
     * @return 申请结果
     */
    @Override
    @Transactional
    @CacheEvict(key = "'no_' + #orderNo")
    public boolean applyRefund(String orderNo, String refundReason) {
        return ordersMapper.applyRefund(orderNo, refundReason) > 0;
    }

    /**
     * 完成订单
     * <p>
     * 该方法用于系统自动完成已发货一定时间的订单，
     * 或用户确认收货后完成订单
     *
     * @param orderNo 订单号
     * @return 完成结果
     */
    @Override
    @Transactional
    @CacheEvict(key = "'no_' + #orderNo")
    public boolean completeOrder(String orderNo) {
        return ordersMapper.completeOrder(orderNo) > 0;
    }

    /**
     * 获取订单超时未支付数量
     * <p>
     * 该方法统计超过指定时间未支付的订单数量，
     * 用于系统监控和自动取消任务
     *
     * @param minutes 超时分钟数
     * @return 超时订单数量
     */
    @Override
    public int countTimeoutOrders(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -minutes);
        Date timeoutTime = calendar.getTime();

        return ordersMapper.countTimeoutOrders(timeoutTime);
    }

    /**
     * 自动取消超时未支付订单
     * <p>
     * 该方法自动取消超过指定时间未支付的订单，
     * 通常由定时任务调用，释放商品库存
     *
     * @param minutes 超时分钟数
     * @return 取消的订单数量
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int autoCancelTimeoutOrders(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -minutes);
        Date timeoutTime = calendar.getTime();

        // 这里可以先查询出所有超时订单，然后释放库存、恢复优惠券等
        return ordersMapper.autoCancelTimeoutOrders(timeoutTime);
    }
}




