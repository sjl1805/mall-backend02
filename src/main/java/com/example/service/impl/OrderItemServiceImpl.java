package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrderItemMapper;
import com.example.model.entity.OrderItem;
import com.example.service.OrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单商品服务实现类
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Override
    public List<OrderItem> getByOrderId(Long orderId) {
        return baseMapper.selectByOrderId(orderId);
    }

    @Override
    public List<OrderItem> getByOrderIds(List<Long> orderIds) {
        return baseMapper.selectByOrderIds(orderIds);
    }

    @Override
    public List<OrderItem> getByProductId(Long productId) {
        return baseMapper.selectByProductId(productId);
    }

    @Override
    public List<OrderItem> getBySkuId(Long skuId) {
        return baseMapper.selectBySkuId(skuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return false;
        }
        
        // 计算每个订单项的总金额
        for (OrderItem item : orderItems) {
            if (item.getTotalAmount() == null) {
                item.setTotalAmount(calculateItemAmount(item.getPrice(), item.getQuantity()));
            }
        }
        
        return baseMapper.batchInsert(orderItems) > 0;
    }

    @Override
    public Integer countProductSales(Long productId) {
        return baseMapper.countSalesByProductId(productId);
    }

    @Override
    public BigDecimal sumProductAmount(Long productId) {
        return baseMapper.sumAmountByProductId(productId);
    }

    @Override
    public Map<Long, Map<String, Object>> getHotProducts(Integer limit) {
        return baseMapper.selectHotProducts(limit);
    }

    @Override
    public List<Map<String, Object>> getProductSalesByDateRange(
            LocalDateTime startTime, 
            LocalDateTime endTime, 
            Integer limit) {
        return baseMapper.selectProductSalesByDateRange(startTime, endTime, limit);
    }

    @Override
    public IPage<OrderItem> getProductOrderItemsPage(Long productId, Page<OrderItem> page) {
        return baseMapper.selectPageByProductId(page, productId);
    }

    @Override
    public Integer countUserProductTypes(Long userId) {
        return baseMapper.countProductTypesByUserId(userId);
    }

    @Override
    public List<OrderItem> getUserRecentPurchases(Long userId, Integer limit) {
        return baseMapper.selectRecentPurchasesByUserId(userId, limit);
    }

    @Override
    public Integer countUserProductPurchases(Long userId, Long productId) {
        return baseMapper.countUserProductPurchases(userId, productId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderItemInfo(OrderItem orderItem) {
        // 确保总金额是正确的
        if (orderItem.getPrice() != null && orderItem.getQuantity() != null) {
            orderItem.setTotalAmount(calculateItemAmount(orderItem.getPrice(), orderItem.getQuantity()));
        }
        return updateById(orderItem);
    }

    @Override
    public BigDecimal calculateItemAmount(BigDecimal price, Integer quantity) {
        return price.multiply(new BigDecimal(quantity));
    }
} 