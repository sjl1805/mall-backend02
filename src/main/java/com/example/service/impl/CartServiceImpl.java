package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.CartMapper;
import com.example.model.dto.cart.CartDTO;
import com.example.model.entity.Cart;
import com.example.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【cart(购物车表)】的数据库操作Service实现
 * @createDate 2025-02-18 23:44:32
 */
@Service
@CacheConfig(cacheNames = "cartService")
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
        implements CartService {

    //private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean addToCart(Long userId, CartDTO cartDTO) {
        // 检查是否已存在
        if (baseMapper.checkCartItemExists(userId, cartDTO.getProductId()) > 0) {
            throw new BusinessException(ResultCode.CART_ITEM_EXISTS);
        }

        Cart cart = new Cart();
        BeanUtils.copyProperties(cartDTO, cart);
        cart.setUserId(userId);
        return save(cart);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updateQuantity(Long userId, Long cartId, Integer delta) {
        return baseMapper.adjustQuantity(userId, cartId, delta) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean batchCheckItems(Long userId, List<Long> productIds, Integer checked) {
        return baseMapper.batchUpdateChecked(userId, productIds, checked) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean clearChecked(Long userId) {
        return baseMapper.clearCheckedItems(userId) > 0;
    }

    @Override
    @Cacheable(key = "'user:' + #userId")
    public List<Cart> getUserCart(Long userId) {
        return lambdaQuery()
                .eq(Cart::getUserId, userId)
                .orderByDesc(Cart::getUpdateTime)
                .list();
    }

    @Override
    @Cacheable(key = "'user:' + #userId + ':count'")
    public int getCartItemCount(Long userId) {
        return baseMapper.countCartItems(userId);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean removeCartItems(Long userId, List<Long> cartIds) {
        return baseMapper.batchDelete(cartIds) > 0;
    }
}




