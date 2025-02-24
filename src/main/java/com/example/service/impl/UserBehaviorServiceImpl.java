package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserBehaviorMapper;
import com.example.model.entity.UserBehavior;
import com.example.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_behavior(用户行为记录表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:47
 */
@Service
@CacheConfig(cacheNames = "userBehaviors")
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior>
        implements UserBehaviorService {

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    @Override
    @Cacheable(value = "userBehaviors", key = "#userId")
    public List<UserBehavior> selectByUserId(Long userId) {
        return userBehaviorMapper.selectByUserId(userId);
    }

    @Override
    public IPage<UserBehavior> selectPage(IPage<UserBehavior> page) {
        return userBehaviorMapper.selectPage(page);
    }

    @Override
    public UserBehavior selectById(Long id) {
        return userBehaviorMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "userBehaviors", key = "#userBehavior.userId")
    public boolean insertUserBehavior(UserBehavior userBehavior) {
        return userBehaviorMapper.insert(userBehavior) > 0;
    }

    @Override
    @CacheEvict(value = "userBehaviors", key = "#userBehavior.userId")
    public boolean updateUserBehavior(UserBehavior userBehavior) {
        return userBehaviorMapper.updateById(userBehavior) > 0;
    }

    @Override
    @CacheEvict(value = "userBehaviors", key = "#id")
    public boolean deleteUserBehavior(Long id) {
        return userBehaviorMapper.deleteById(id) > 0;
    }
}




