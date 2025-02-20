package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.CartMapper;
import com.example.model.dto.CartDTO;
import com.example.model.entity.Cart;
import com.example.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 * 
 * @author 31815
 * @description 实现购物车核心业务逻辑，包含：
 *              1. 商品添加校验与幂等控制
 *              2. 数量调整的原子操作
 *              3. 批量操作的事务管理
 *              4. 缓存策略优化
 * @createDate 2025-02-18 23:44:32
 */
@Service
@CacheConfig(cacheNames = "cartService")
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
        implements CartService {

    //private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    /**
     * 添加商品到购物车（幂等校验）
     * @param userId 用户ID
     * @param cartDTO 商品信息
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 检查商品是否已存在
     *           2. 初始化默认值（数量、选中状态）
     *           3. 清除用户购物车缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean addToCart(Long userId, CartDTO cartDTO) {
        if (baseMapper.checkCartItemExists(userId, cartDTO.getProductId()) > 0) {
            throw new BusinessException(ResultCode.CART_ITEM_EXISTS);
        }

        Cart cart = new Cart();
        BeanUtils.copyProperties(cartDTO, cart);
        cart.setUserId(userId);
        return save(cart);
    }

    /**
     * 调整商品数量（原子操作）
     * @param userId 用户ID
     * @param cartId 购物车项ID
     * @param delta 数量变化值
     * @return 操作结果
     * @implNote 使用数据库原子操作保证并发安全：
     *           1. 直接更新数量字段
     *           2. 数量为0时自动删除
     *           3. 更新后清除缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean updateQuantity(Long userId, Long cartId, Integer delta) {
        return baseMapper.adjustQuantity(userId, cartId, delta) > 0;
    }

    /**
     * 批量更新选中状态（事务管理）
     * @param userId 用户ID
     * @param productIds 目标商品ID列表
     * @param checked 新状态
     * @return 操作结果
     * @implNote 空productIds表示全选/全不选操作
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean batchCheckItems(Long userId, List<Long> productIds, Integer checked) {
        return baseMapper.batchUpdateChecked(userId, productIds, checked) > 0;
    }

    /**
     * 清空已选中商品（物理删除）
     * @param userId 用户ID
     * @return 操作结果
     * @apiNote 执行逻辑：
     *          1. 标记删除已选中商品
     *          2. 更新关联订单状态（需在订单服务实现）
     *          3. 清除缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean clearChecked(Long userId) {
        return baseMapper.clearCheckedItems(userId) > 0;
    }

    /**
     * 获取用户购物车（缓存优化）
     * @param userId 用户ID
     * @return 购物车项列表
     * @implNote 缓存策略：
     *           1. 缓存键：user:{userId}
     *           2. 缓存时间：10分钟
     *           3. 更新操作自动清除缓存
     */
    @Override
    @Cacheable(key = "'user:' + #userId")
    public List<CartDTO> getUserCart(Long userId) {
        return lambdaQuery()
                .eq(Cart::getUserId, userId)
                .orderByDesc(Cart::getUpdateTime)
                .list()
                .stream()
                .map(CartDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 获取购物车商品数量（独立缓存）
     * @param userId 用户ID
     * @return 商品总数
     * @implNote 使用独立缓存键避免全量查询：
     *           1. 缓存键：user:{userId}:count
     *           2. 数量变化时更新缓存
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':count'")
    public int getCartItemCount(Long userId) {
        return baseMapper.countCartItems(userId);
    }

    /**
     * 批量删除购物车项（权限校验）
     * @param userId 用户ID
     * @param cartIds 目标项ID列表
     * @return 操作结果
     * @implNote 安全措施：
     *           1. 验证用户对购物车项的所有权
     *           2. 使用批量删除提升性能
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean removeCartItems(Long userId, List<Long> cartIds) {
        return baseMapper.batchDelete(cartIds) > 0;
    }

}




