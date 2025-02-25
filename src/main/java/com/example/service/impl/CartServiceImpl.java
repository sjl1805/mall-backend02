package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.CartMapper;
import com.example.model.entity.Cart;
import com.example.model.entity.Product;
import com.example.model.entity.ProductSku;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车服务实现类
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductSkuService productSkuService;

    @Override
    public List<Cart> getUserCarts(Long userId) {
        return baseMapper.findByUserId(userId);
    }

    @Override
    public List<Cart> getCheckedCarts(Long userId) {
        return baseMapper.findCheckedByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cart addToCart(Long userId, Long productId, Long skuId, Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("商品数量必须大于0");
        }
        
        // 检查商品是否存在
        Product product = productService.getById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new BusinessException("商品不存在或已下架");
        }
        
        // 检查SKU是否存在
        ProductSku sku = productSkuService.getById(skuId);
        if (sku == null || sku.getStatus() != 1 || !sku.getProductId().equals(productId)) {
            throw new BusinessException("商品规格不存在");
        }
        
        // 检查库存
        if (sku.getStock() < quantity) {
            throw new BusinessException("商品库存不足");
        }
        
        // 检查是否已在购物车中
        Cart existCart = baseMapper.findExistCart(userId, productId, skuId);
        
        if (existCart != null) {
            // 更新购物车数量
            int newQuantity = existCart.getQuantity() + quantity;
            if (sku.getStock() < newQuantity) {
                throw new BusinessException("商品库存不足");
            }
            
            LambdaUpdateWrapper<Cart> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Cart::getId, existCart.getId())
                    .set(Cart::getQuantity, newQuantity)
                    .set(Cart::getUpdateTime, LocalDateTime.now());
            update(updateWrapper);
            
            existCart.setQuantity(newQuantity);
            existCart.setUpdateTime(LocalDateTime.now());
            return existCart;
        } else {
            // 添加新购物车项
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setSkuId(skuId);
            cart.setQuantity(quantity);
            cart.setChecked(1); // 默认选中
            cart.setPrice(sku.getPrice());
            cart.setSkuProperties(sku.getProperties());
            cart.setProductName(product.getName());
            cart.setProductImage(product.getMainImage());
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            
            save(cart);
            return cart;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCartQuantity(Long cartId, Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("商品数量必须大于0");
        }
        
        // 查询购物车项
        Cart cart = getById(cartId);
        if (cart == null) {
            throw new BusinessException("购物车项不存在");
        }
        
        // 检查库存
        ProductSku sku = productSkuService.getById(cart.getSkuId());
        if (sku == null || sku.getStock() < quantity) {
            throw new BusinessException("商品库存不足");
        }
        
        return baseMapper.updateQuantity(cartId, quantity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCartChecked(Long cartId, Integer checked) {
        if (checked != 0 && checked != 1) {
            throw new BusinessException("选中状态参数错误");
        }
        
        LambdaUpdateWrapper<Cart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Cart::getId, cartId)
                .set(Cart::getChecked, checked)
                .set(Cart::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkAllCarts(Long userId, Integer checked) {
        if (checked != 0 && checked != 1) {
            throw new BusinessException("选中状态参数错误");
        }
        
        return baseMapper.updateCheckedByUserId(userId, checked) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCart(Long cartId) {
        return removeById(cartId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearUserCarts(Long userId) {
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, userId);
        return remove(queryWrapper);
    }
} 