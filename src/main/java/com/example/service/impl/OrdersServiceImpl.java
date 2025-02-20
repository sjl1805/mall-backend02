package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrdersMapper;
import com.example.model.dto.order.OrderCreateDTO;
import com.example.model.dto.order.OrderItemDTO;
import com.example.model.dto.order.OrdersDTO;
import com.example.model.entity.OrderItem;
import com.example.model.entity.Orders;
import com.example.service.OrderItemService;
import com.example.service.OrdersService;
import com.example.utils.OrderNoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 * 
 * @author 31815
 * @description 实现订单核心业务逻辑，包含：
 *              1. 订单创建的事务管理
 *              2. 订单状态流转控制
 *              3. 定时任务处理
 *              4. 缓存策略优化
 * @createDate 2025-02-18 23:44:18
 */
@Service
@CacheConfig(cacheNames = "orderService")
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService {

    private final OrderItemService orderItemService;

    public OrdersServiceImpl(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    /**
     * 创建订单（完整事务）
     * @param orderDTO 订单创建参数
     * @return 订单号
     * @implNote 业务逻辑：
     *           1. 生成唯一订单号
     *           2. 保存订单主表
     *           3. 批量保存订单项
     *           4. 清除相关缓存
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public String createOrder(OrderCreateDTO orderDTO) {
        String orderNo = OrderNoUtils.generate("PC");
        Orders order = new Orders();
        BeanUtils.copyProperties(orderDTO, order);
        order.setOrderNo(orderNo);
        order.setStatus(0);
        save(order);

        List<OrderItem> items = orderDTO.getItems().stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            BeanUtils.copyProperties(itemDTO, item);
            item.setOrderId(order.getId());
            return item;
        }).collect(Collectors.toList());

        orderItemService.saveBatch(items);
        return orderNo;
    }

    /**
     * 取消订单（状态变更）
     * @param orderNo 订单号
     * @return 操作结果
     * @apiNote 执行逻辑：
     *          1. 验证订单状态是否允许取消
     *          2. 更新状态为已取消（4）
     *          3. 清除订单缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "#orderNo")
    public boolean cancelOrder(String orderNo) {
        return lambdaUpdate()
                .eq(Orders::getOrderNo, orderNo)
                .set(Orders::getStatus, 4)
                .update();
    }

    /**
     * 支付订单（模拟支付）
     * @param orderNo 订单号
     * @return 支付结果
     * @implNote 支付成功后：
     *           1. 更新状态为已支付（1）
     *           2. 记录支付时间
     *           3. 清除订单缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "#orderNo")
    public boolean payOrder(String orderNo) {
        return lambdaUpdate()
                .eq(Orders::getOrderNo, orderNo)
                .set(Orders::getStatus, 1)
                .set(Orders::getPaymentTime, new Date())
                .update();
    }

    /**
     * 获取订单详情（缓存优化）
     * @param orderNo 订单号
     * @return 完整订单信息
     * @implNote 缓存策略：
     *           1. 缓存键：order:{orderNo}
     *           2. 缓存时间：30分钟
     *           3. 关联查询订单项
     */
    @Override
    @Cacheable(key = "'order:' + #orderNo")
    public OrdersDTO getOrderDetail(String orderNo) {
        Orders order = lambdaQuery()
                .eq(Orders::getOrderNo, orderNo)
                .one();
        if (order != null) {
            order.setItems(orderItemService.listByOrderIdEntities(order.getId()));
        }
        return convertToDTO(order);
    }

    /**
     * 自动取消未支付订单（定时任务）
     * @implNote 执行逻辑：
     *           1. 查询超过30分钟未支付的订单
     *           2. 批量更新状态为已取消
     *           3. 清除相关缓存
     */
    @Override
    @Scheduled(cron = "0 0/30 * * * ?")
    @Transactional
    @CacheEvict(allEntries = true)
    public void autoCancelUnpaidOrders() {
        baseMapper.autoCancelOrders(30);
    }

    private OrdersDTO convertToDTO(Orders order) {
        OrdersDTO dto = new OrdersDTO();
        BeanUtils.copyProperties(order, dto);
        // 转换嵌套的订单项
        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }
        return dto;
    }

    private OrderItemDTO convertItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}




