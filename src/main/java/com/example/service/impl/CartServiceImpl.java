package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CartMapper;
import com.example.model.entity.Cart;
import com.example.service.CartService;
import com.example.service.UserBehaviorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车服务实现类
 */
@Slf4j
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private UserBehaviorService userBehaviorService;

    @Override
    @Transactional
    public boolean addToCart(Long userId, Long productId, Long skuId, Integer quantity) {
        // 检查购物车中是否已存在该商品
        Cart existingCart = cartMapper.selectByUserIdAndProductId(userId, productId, skuId);
        
        if (existingCart != null) {
            // 已存在，更新数量
            int newQuantity = existingCart.getQuantity() + quantity;
            return cartMapper.updateQuantity(existingCart.getId(), newQuantity) > 0;
        } else {
            // 不存在，新增购物车项
            Cart cart = Cart.builder()
                    .userId(userId)
                    .productId(productId)
                    .skuId(skuId)
                    .quantity(quantity)
                    .checked(1) // 默认选中
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            
            boolean result = save(cart);
            
            // 记录用户加入购物车行为
            if (result) {
                try {
                    userBehaviorService.recordCartBehavior(userId, productId, null);
                } catch (Exception e) {
                    log.error("记录用户加入购物车行为失败", e);
                    // 不影响主流程
                }
            }
            
            return result;
        }
    }

    @Override
    public boolean updateQuantity(Long id, Integer quantity) {
        if (quantity <= 0) {
            // 数量小于等于0，删除购物车项
            return removeCartItem(id);
        }
        return cartMapper.updateQuantity(id, quantity) > 0;
    }

    @Override
    public boolean updateCheckedStatus(Long id, Integer checked) {
        return cartMapper.updateCheckedStatus(id, checked) > 0;
    }

    @Override
    public boolean updateAllCheckedStatus(Long userId, Integer checked) {
        return cartMapper.updateAllCheckedStatus(userId, checked) > 0;
    }

    @Override
    public boolean removeCartItem(Long id) {
        return cartMapper.deleteById(id) > 0;
    }

    @Override
    public boolean clearCart(Long userId) {
        return cartMapper.clearCart(userId) > 0;
    }

    @Override
    public List<Cart> getUserCartList(Long userId) {
        return cartMapper.selectByUserId(userId);
    }

    @Override
    public int getCartItemCount(Long userId) {
        return cartMapper.countItems(userId);
    }

    @Override
    public List<Cart> getCheckedItems(Long userId) {
        return cartMapper.selectCheckedItems(userId);
    }

    @Override
    @Transactional
    public boolean batchAddToCart(List<Cart> cartList) {
        if (cartList == null || cartList.isEmpty()) {
            return false;
        }
        
        // 设置创建时间和更新时间
        for (Cart cart : cartList) {
            if (cart.getCreateTime() == null) {
                cart.setCreateTime(LocalDateTime.now());
            }
            if (cart.getUpdateTime() == null) {
                cart.setUpdateTime(LocalDateTime.now());
            }
            if (cart.getChecked() == null) {
                cart.setChecked(1); // 默认选中
            }
        }
        
        return cartMapper.batchInsert(cartList) > 0;
    }

    @Override
    public Map<String, Object> getCartAmount(Long userId) {
        Map<String, Object> result = cartMapper.selectCartAmount(userId);
        if (result == null) {
            result = new HashMap<>();
            result.put("item_count", 0);
            result.put("product_count", 0);
            result.put("total_amount", BigDecimal.ZERO);
        }
        return result;
    }

    @Override
    public boolean existsCartItem(Long userId, Long productId, Long skuId) {
        return cartMapper.existsCartItem(userId, productId, skuId) > 0;
    }

    @Override
    public boolean removeCheckedItems(Long userId) {
        return cartMapper.deleteCheckedItems(userId) > 0;
    }

    @Override
    @Transactional
    public boolean mergeCart(Long fromUserId, Long toUserId) {
        // 先检查源用户是否有购物车项
        List<Cart> fromUserCart = cartMapper.selectByUserId(fromUserId);
        if (fromUserCart == null || fromUserCart.isEmpty()) {
            return true; // 源用户没有购物车项，视为合并成功
        }
        
        // 执行合并
        int result = cartMapper.mergeCart(fromUserId, toUserId);
        
        // 清空源用户购物车
        if (result > 0) {
            cartMapper.clearCart(fromUserId);
        }
        
        return result > 0;
    }

    @Override
    public Cart getCartItem(Long userId, Long productId, Long skuId) {
        return cartMapper.selectByUserIdAndProductId(userId, productId, skuId);
    }

    @Override
    public BigDecimal calculateCheckedItemsTotal(Long userId) {
        Map<String, Object> amountInfo = getCartAmount(userId);
        Object totalAmount = amountInfo.get("total_amount");
        
        if (totalAmount instanceof BigDecimal) {
            return (BigDecimal) totalAmount;
        } else if (totalAmount instanceof Number) {
            return new BigDecimal(totalAmount.toString());
        }
        
        return BigDecimal.ZERO;
    }

    @Override
    public List<Long> checkCartItemStock(Long userId) {
        // 获取用户购物车中已选中的商品
        List<Cart> checkedItems = getCheckedItems(userId);
        List<Long> insufficientStockItems = new ArrayList<>();
        
        // 这里需要调用商品服务检查库存，暂时模拟实现
        // 实际项目中应该注入商品服务，调用相关方法检查库存
        for (Cart item : checkedItems) {
            // 模拟检查库存的逻辑
            boolean hasStock = checkProductStock(item.getProductId(), item.getSkuId(), item.getQuantity());
            if (!hasStock) {
                insufficientStockItems.add(item.getProductId());
            }
        }
        
        return insufficientStockItems;
    }
    
    /**
     * 模拟检查商品库存
     * 实际项目中应该调用商品服务的方法
     */
    private boolean checkProductStock(Long productId, Long skuId, Integer quantity) {
        // 模拟实现，实际项目中应该调用商品服务
        return true;
    }

    @Override
    public int getCartItemTypeCount(Long userId) {
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, userId);
        return (int) count(queryWrapper);
    }
} 