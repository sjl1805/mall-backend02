package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CartMapper;
import com.example.model.entity.Cart;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【cart(购物车表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:25
 */
@Service
@CacheConfig(cacheNames = "carts")
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
        implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    @Cacheable(value = "carts", key = "#userId")
    public List<Cart> selectByUserId(Long userId) {
        return cartMapper.selectByUserId(userId);
    }

    @Override
    public IPage<Cart> selectPage(IPage<Cart> page) {
        return cartMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "carts", key = "#id")
    public Cart selectById(Long id) {
        return cartMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "carts", key = "#cart.userId")
    public boolean insertCart(Cart cart) {
        return cartMapper.insert(cart) > 0;
    }

    @Override
    @CacheEvict(value = "carts", key = "#cart.userId")
    public boolean updateCart(Cart cart) {
        return cartMapper.updateById(cart) > 0;
    }

    @Override
    @CacheEvict(value = "carts", key = "#id")
    public boolean deleteCart(Long id) {
        return cartMapper.deleteById(id) > 0;
    }
}




