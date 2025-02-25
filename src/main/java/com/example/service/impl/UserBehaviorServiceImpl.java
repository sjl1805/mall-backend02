package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.UserBehaviorMapper;
import com.example.model.entity.UserBehavior;
import com.example.service.UserBehaviorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户行为服务实现类
 */
@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior> implements UserBehaviorService {

    @Override
    public List<UserBehavior> getUserBehaviors(Long userId, Integer behaviorType) {
        return baseMapper.findByUserIdAndType(userId, behaviorType);
    }

    @Override
    public List<UserBehavior> getProductBehaviors(Long productId, Integer behaviorType) {
        return baseMapper.findByProductIdAndType(productId, behaviorType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserBehavior recordBehavior(Long userId, Long productId, Integer behaviorType) {
        if (behaviorType < 1 || behaviorType > 4) {
            throw new BusinessException("行为类型参数错误");
        }
        
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setProductId(productId);
        behavior.setBehaviorType(behaviorType);
        behavior.setCreateTime(LocalDateTime.now());
        
        save(behavior);
        
        return behavior;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserBehavior recordView(Long userId, Long productId) {
        return recordBehavior(userId, productId, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserBehavior recordFavorite(Long userId, Long productId) {
        return recordBehavior(userId, productId, 2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserBehavior recordAddToCart(Long userId, Long productId) {
        return recordBehavior(userId, productId, 3);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserBehavior recordPurchase(Long userId, Long productId) {
        return recordBehavior(userId, productId, 4);
    }

    @Override
    public int countUserBehavior(Long userId, Integer behaviorType) {
        return baseMapper.countByUserIdAndType(userId, behaviorType);
    }
} 