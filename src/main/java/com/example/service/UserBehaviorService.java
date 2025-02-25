package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserBehavior;

import java.util.List;

/**
 * 用户行为服务接口
 */
public interface UserBehaviorService extends IService<UserBehavior> {
    
    /**
     * 获取用户行为列表
     *
     * @param userId 用户ID
     * @param behaviorType 行为类型：1-浏览 2-收藏 3-加购 4-购买
     * @return 用户行为列表
     */
    List<UserBehavior> getUserBehaviors(Long userId, Integer behaviorType);
    
    /**
     * 获取商品行为列表
     *
     * @param productId 商品ID
     * @param behaviorType 行为类型：1-浏览 2-收藏 3-加购 4-购买
     * @return 商品行为列表
     */
    List<UserBehavior> getProductBehaviors(Long productId, Integer behaviorType);
    
    /**
     * 记录用户行为
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @param behaviorType 行为类型：1-浏览 2-收藏 3-加购 4-购买
     * @return 记录的用户行为
     */
    UserBehavior recordBehavior(Long userId, Long productId, Integer behaviorType);
    
    /**
     * 记录用户浏览行为
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 记录的浏览行为
     */
    UserBehavior recordView(Long userId, Long productId);
    
    /**
     * 记录用户收藏行为
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 记录的收藏行为
     */
    UserBehavior recordFavorite(Long userId, Long productId);
    
    /**
     * 记录用户加购行为
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 记录的加购行为
     */
    UserBehavior recordAddToCart(Long userId, Long productId);
    
    /**
     * 记录用户购买行为
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 记录的购买行为
     */
    UserBehavior recordPurchase(Long userId, Long productId);
    
    /**
     * 统计用户行为数量
     *
     * @param userId 用户ID
     * @param behaviorType 行为类型
     * @return 行为数量
     */
    int countUserBehavior(Long userId, Integer behaviorType);
} 