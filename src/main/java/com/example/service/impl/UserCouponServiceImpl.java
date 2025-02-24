package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.UserCouponMapper;
import com.example.model.entity.UserCoupon;
import com.example.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_coupon(用户优惠券表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:03:44
 */
@Service
@CacheConfig(cacheNames = "userCoupons")
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon>
        implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    @Cacheable(value = "userCoupons", key = "#userId")
    public List<UserCoupon> selectByUserId(Long userId) {
        return userCouponMapper.selectByUserId(userId);
    }

    @Override
    @CacheEvict(value = "userCoupons", key = "#userCoupon.userId")
    public boolean insertUserCoupon(UserCoupon userCoupon) {
        return userCouponMapper.insert(userCoupon) > 0;
    }

    @Override
    @CacheEvict(value = "userCoupons", key = "#userCoupon.userId")
    public boolean updateUserCoupon(UserCoupon userCoupon) {
        return userCouponMapper.updateById(userCoupon) > 0;
    }

    @Override
    @CacheEvict(value = "userCoupons", key = "#id")
    public boolean deleteUserCoupon(Long id) {
        return userCouponMapper.deleteById(id) > 0;
    }

    @Override
    public IPage<UserCoupon> selectPage(IPage<UserCoupon> page) {
        return userCouponMapper.selectPage(page);
    }

    @Override
    public UserCoupon selectById(Long id) {
        return userCouponMapper.selectById(id);
    }

    @Override
    public List<UserCoupon> selectByUserIdAndStatus(Long userId, String status) {
        return userCouponMapper.selectByUserIdAndStatus(userId, status);
    }
}




