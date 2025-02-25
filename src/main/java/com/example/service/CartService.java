package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Cart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService extends IService<Cart> {
    
    /**
     * 获取用户购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> getUserCarts(Long userId);
    
    /**
     * 获取用户已选中的购物车列表
     *
     * @param userId 用户ID
     * @return 已选中的购物车列表
     */
    List<Cart> getCheckedCarts(Long userId);
    
    /**
     * 添加商品到购物车
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @param skuId SKU ID
     * @param quantity 数量
     * @return 添加成功的购物车项
     */
    Cart addToCart(Long userId, Long productId, Long skuId, Integer quantity);
    
    /**
     * 更新购物车商品数量
     *
     * @param cartId 购物车ID
     * @param quantity 数量
     * @return 是否更新成功
     */
    boolean updateCartQuantity(Long cartId, Integer quantity);
    
    /**
     * 更新购物车选中状态
     *
     * @param cartId 购物车ID
     * @param checked 是否选中：0-未选中 1-已选中
     * @return 是否更新成功
     */
    boolean updateCartChecked(Long cartId, Integer checked);
    
    /**
     * 全选/取消全选购物车
     *
     * @param userId 用户ID
     * @param checked 是否选中：0-未选中 1-已选中
     * @return 是否更新成功
     */
    boolean checkAllCarts(Long userId, Integer checked);
    
    /**
     * 删除购物车项
     *
     * @param cartId 购物车ID
     * @return 是否删除成功
     */
    boolean deleteCart(Long cartId);
    
    /**
     * 清空用户购物车
     *
     * @param userId 用户ID
     * @return 是否清空成功
     */
    boolean clearUserCarts(Long userId);
} 