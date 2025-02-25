package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 购物车服务接口
 */
public interface CartService extends IService<Cart> {
    
    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param productId 商品ID
     * @param skuId SKU ID
     * @param quantity 数量
     * @return 是否成功
     */
    boolean addToCart(Long userId, Long productId, Long skuId, Integer quantity);
    
    /**
     * 更新购物车商品数量
     * @param id 购物车ID
     * @param quantity 新数量
     * @return 是否成功
     */
    boolean updateQuantity(Long id, Integer quantity);
    
    /**
     * 更新购物车项选中状态
     * @param id 购物车ID
     * @param checked 选中状态
     * @return 是否成功
     */
    boolean updateCheckedStatus(Long id, Integer checked);
    
    /**
     * 批量更新购物车项选中状态
     * @param userId 用户ID
     * @param checked 选中状态
     * @return 是否成功
     */
    boolean updateAllCheckedStatus(Long userId, Integer checked);
    
    /**
     * 删除购物车项
     * @param id 购物车ID
     * @return 是否成功
     */
    boolean removeCartItem(Long id);
    
    /**
     * 清空用户购物车
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean clearCart(Long userId);
    
    /**
     * 获取用户购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> getUserCartList(Long userId);
    
    /**
     * 获取用户购物车商品总数
     * @param userId 用户ID
     * @return 购物车商品总数
     */
    int getCartItemCount(Long userId);
    
    /**
     * 获取用户购物车已选中的商品项
     * @param userId 用户ID
     * @return 已选中的购物车项列表
     */
    List<Cart> getCheckedItems(Long userId);
    
    /**
     * 批量添加购物车项
     * @param cartList 购物车项列表
     * @return 是否成功
     */
    boolean batchAddToCart(List<Cart> cartList);
    
    /**
     * 获取购物车商品总价
     * @param userId 用户ID
     * @return 购物车商品总价信息，包含商品数量、总价等
     */
    Map<String, Object> getCartAmount(Long userId);
    
    /**
     * 检查购物车项是否存在
     * @param userId 用户ID
     * @param productId 商品ID
     * @param skuId SKU ID
     * @return 是否存在
     */
    boolean existsCartItem(Long userId, Long productId, Long skuId);
    
    /**
     * 删除已选中的购物车项
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean removeCheckedItems(Long userId);
    
    /**
     * 合并购物车
     * @param fromUserId 源用户ID
     * @param toUserId 目标用户ID
     * @return 是否成功
     */
    boolean mergeCart(Long fromUserId, Long toUserId);
    
    /**
     * 获取购物车中指定商品
     * @param userId 用户ID
     * @param productId 商品ID
     * @param skuId SKU ID
     * @return 购物车项
     */
    Cart getCartItem(Long userId, Long productId, Long skuId);
    
    /**
     * 计算购物车中选中商品的总价
     * @param userId 用户ID
     * @return 总价
     */
    BigDecimal calculateCheckedItemsTotal(Long userId);
    
    /**
     * 检查购物车中商品库存是否充足
     * @param userId 用户ID
     * @return 库存不足的商品ID列表，如果都充足则返回空列表
     */
    List<Long> checkCartItemStock(Long userId);
    
    /**
     * 获取购物车中商品种类数量
     * @param userId 用户ID
     * @return 商品种类数量
     */
    int getCartItemTypeCount(Long userId);
} 