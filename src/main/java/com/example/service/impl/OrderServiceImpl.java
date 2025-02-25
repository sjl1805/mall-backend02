package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import com.example.model.entity.*;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private UserAddressService userAddressService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductSkuService productSkuService;
    
    @Autowired
    private UserCouponService userCouponService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long userId, Long addressId, Long couponId, String note) {
        // 获取用户收货地址
        UserAddress address = userAddressService.getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("收货地址不存在");
        }
        
        // 获取购物车中已选中的商品
        List<Cart> carts = cartService.getCheckedCarts(userId);
        if (carts.isEmpty()) {
            throw new BusinessException("购物车中没有选中的商品");
        }
        
        // 检查商品库存并计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (Cart cart : carts) {
            ProductSku sku = productSkuService.getById(cart.getSkuId());
            if (sku == null || sku.getStatus() != 1) {
                throw new BusinessException("商品规格不存在或已下架: " + cart.getProductName());
            }
            
            if (sku.getStock() < cart.getQuantity()) {
                throw new BusinessException("商品库存不足: " + cart.getProductName());
            }
            
            // 计算商品总价
            BigDecimal itemTotal = sku.getPrice().multiply(new BigDecimal(cart.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            
            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cart.getProductId());
            orderItem.setProductName(cart.getProductName());
            orderItem.setProductImage(cart.getProductImage());
            orderItem.setSkuId(cart.getSkuId());
            orderItem.setSkuProperties(cart.getSkuProperties());
            orderItem.setPrice(sku.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalAmount(itemTotal);
            
            orderItems.add(orderItem);
        }
        
        // 应用优惠券
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (couponId != null) {
            UserCoupon userCoupon = userCouponService.getUserCoupon(userId, couponId);
            if (userCoupon == null) {
                throw new BusinessException("优惠券不存在或已使用");
            }
            
            // 计算优惠金额（这里简化处理）
            Coupon coupon = userCouponService.getCouponInfo(couponId);
            if (coupon.getThreshold() != null && totalAmount.compareTo(coupon.getThreshold()) < 0) {
                throw new BusinessException("订单金额未满足优惠券使用条件");
            }
            
            if (coupon.getType() == 1) {
                // 满减券
                discountAmount = coupon.getAmount();
            } else if (coupon.getType() == 2) {
                // 折扣券
                BigDecimal discount = BigDecimal.ONE.subtract(coupon.getAmount());
                discountAmount = totalAmount.multiply(discount).setScale(2, RoundingMode.HALF_UP);
            } else if (coupon.getType() == 3) {
                // 无门槛券
                discountAmount = coupon.getAmount();
            }
        }
        
        // 计算最终支付金额
        BigDecimal payAmount = totalAmount.subtract(discountAmount);
        if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }
        
        // 生成订单
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo(generateOrderNo());
        order.setTotalAmount(totalAmount);
        order.setPayAmount(payAmount);
        order.setDiscountAmount(discountAmount);
        order.setStatus(0); // 待支付
        order.setPaymentType(0); // 未支付
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverDistrict(address.getDistrict());
        order.setReceiverAddress(address.getDetailAddress());
        order.setNote(note);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 保存订单
        save(order);
        
        // 保存订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            item.setCreateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            orderItemMapper.insert(item);
            
            // 减少商品库存
            productSkuService.decreaseStock(item.getSkuId(), item.getQuantity());
        }
        
        // 使用优惠券
        if (couponId != null) {
            userCouponService.useCoupon(userId, couponId);
        }
        
        // 清空购物车中已选中的商品
        for (Cart cart : carts) {
            cartService.deleteCart(cart.getId());
        }
        
        return order;
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        return baseMapper.findByOrderNo(orderNo);
    }

    @Override
    public IPage<Order> getUserOrders(Long userId, Page<Order> page) {
        return baseMapper.findByUserId(page, userId);
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.findByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        
        if (order.getStatus() != 0) {
            throw new BusinessException("订单状态不允许取消");
        }
        
        // 取消优惠券使用
        userCouponService.cancelUseCoupon(orderId);
        
        // 恢复商品库存
        List<OrderItem> items = getOrderItems(orderId);
        for (OrderItem item : items) {
            productSkuService.increaseStock(item.getSkuId(), item.getQuantity());
        }
        
        // 更新订单状态为已取消
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Order::getId, orderId)
                .set(Order::getStatus, 5) // 已取消
                .set(Order::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(Long userId, Long orderId, Integer paymentType) {
        if (paymentType != 1 && paymentType != 2) {
            throw new BusinessException("支付方式错误");
        }
        
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        
        if (order.getStatus() != 0) {
            throw new BusinessException("订单状态不允许支付");
        }
        
        // 更新订单状态为已支付
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Order::getId, orderId)
                .set(Order::getStatus, 1) // 已支付
                .set(Order::getPaymentType, paymentType)
                .set(Order::getPayTime, LocalDateTime.now())
                .set(Order::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        
        if (order.getStatus() != 2 && order.getStatus() != 3) {
            throw new BusinessException("订单状态不允许确认收货");
        }
        
        // 更新订单状态为已完成
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Order::getId, orderId)
                .set(Order::getStatus, 4) // 已完成
                .set(Order::getCompleteTime, LocalDateTime.now())
                .set(Order::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        
        // 只允许删除已完成或已取消的订单
        if (order.getStatus() != 4 && order.getStatus() != 5) {
            throw new BusinessException("订单状态不允许删除");
        }
        
        // 更新订单是否删除标志
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Order::getId, orderId)
                .set(Order::getDeleted, 1) // 已删除
                .set(Order::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Long orderId, Integer status) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 检查状态变更是否合法（后续可添加更详细的状态转换规则）
        
        return baseMapper.updateStatus(orderId, status) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoCancelTimeoutOrders() {
        // 查询超过30分钟未支付的订单
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(30);
        queryWrapper.eq(Order::getStatus, 0) // 待支付
                .lt(Order::getCreateTime, timeout);
        
        List<Order> orders = list(queryWrapper);
        int count = 0;
        
        for (Order order : orders) {
            try {
                // 取消订单
                cancelOrder(order.getUserId(), order.getId());
                count++;
            } catch (Exception e) {
                // 记录日志，继续处理下一个订单
                log.error("自动取消订单失败: " + order.getId(), e);
            }
        }
        
        return count;
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        // 时间戳+随机数
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return timestamp + random;
    }
} 