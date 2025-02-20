package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrderItemMapper;
import com.example.model.entity.OrderItem;
import com.example.model.dto.order.OrderItemDTO;
import com.example.service.OrderItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单项服务实现类
 * 
 * @author 31815
 * @description 实现订单项核心业务逻辑，包含：
 *              1. 订单项数据查询
 *              2. 评价状态管理
 *              3. 事务控制
 * @createDate 2025-02-18 23:44:21
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
        implements OrderItemService {

    /**
     * 查询订单项（关联商品信息）
     * @param orderId 订单ID
     * @return 订单项列表（包含商品快照信息）
     * @implNote 使用MyBatis关联查询获取商品快照数据
     */
    @Override
    public List<OrderItemDTO> listByOrderId(Long orderId) {
        return baseMapper.selectByOrderId(orderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 更新评价状态（原子操作）
     * @param orderId 订单ID
     * @param productId 商品ID
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 验证订单状态是否允许评价
     *           2. 更新评价状态为已评价（commentStatus=1）
     */
    @Override
    @Transactional
    public boolean updateCommentStatus(Long orderId, Long productId) {
        return baseMapper.updateCommentStatus(orderId, productId, 1) > 0;
    }

    @Override
    public List<OrderItem> listByOrderIdEntities(Long orderId) {
        return baseMapper.selectByOrderId(orderId);
    }

    private OrderItemDTO convertToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }
}




