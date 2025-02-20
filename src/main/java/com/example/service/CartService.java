package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.CartDTO;
import com.example.model.entity.Cart;

import java.util.List;

/**
 * 购物车服务接口
 * 
 * @author 31815
 * @description 提供购物车核心功能，包含：
 *              1. 商品添加与数量管理
 *              2. 商品选中状态管理
 *              3. 购物车清理与统计
 *              4. 缓存优化与事务控制
 * @createDate 2025-02-18 23:44:32
 */
public interface CartService extends IService<Cart> {

    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param cartDTO 商品信息，包含：
     *                - productId: 商品ID（必须）
     *                - quantity: 初始数量（默认1）
     *                - selected: 是否选中（默认true）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当商品已存在时抛出CART_ITEM_EXISTS
     */
    boolean addToCart(Long userId, CartDTO cartDTO);

    /**
     * 调整商品数量（增量）
     * @param userId 用户ID
     * @param cartId 购物车项ID
     * @param delta 数量变化值（正数增加，负数减少）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当数量为0时自动删除商品
     */
    boolean updateQuantity(Long userId, Long cartId, Integer delta);

    /**
     * 批量更新选中状态
     * @param userId 用户ID
     * @param productIds 商品ID列表（空列表表示全选/全不选）
     * @param checked 新状态（1-选中，0-不选中）
     * @return 更新影响的行数
     */
    boolean batchCheckItems(Long userId, List<Long> productIds, Integer checked);

    /**
     * 清空已选中商品
     * @param userId 用户ID
     * @return 操作是否成功
     * @implNote 实际执行逻辑删除，需处理订单关联
     */
    boolean clearChecked(Long userId);

    /**
     * 获取用户完整购物车
     * @param userId 用户ID
     * @return 按更新时间倒序排列的购物车项列表
     * @implNote 结果缓存优化，有效期10分钟
     */
    List<CartDTO> getUserCart(Long userId);

    /**
     * 获取购物车商品总数
     * @param userId 用户ID
     * @return 有效商品数量（排除已删除项）
     * @implNote 独立缓存计数器，避免频繁查询全量数据
     */
    int getCartItemCount(Long userId);

    /**
     * 批量移除购物车项
     * @param userId 用户ID
     * @param cartIds 要删除的购物车项ID列表
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当尝试删除他人购物车项时抛出
     */
    boolean removeCartItems(Long userId, List<Long> cartIds);
}
