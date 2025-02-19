package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.UserCouponService;
import com.example.model.entity.UserCoupon;
import com.example.exception.BusinessException;
import com.example.common.ResultCode;
import com.example.mapper.UserCouponMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author 31815
* @description 针对表【user_coupon(用户优惠券表)】的数据库操作Service实现
* @createDate 2025-02-18 23:43:48
*/
@Service
@CacheConfig(cacheNames = "userCouponService")
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon>
    implements UserCouponService {

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean acquireCoupon(Long userId, Long couponId) {
        // 检查是否已领取
        if (lambdaQuery()
            .eq(UserCoupon::getUserId, userId)
            .eq(UserCoupon::getCouponId, couponId)
            .exists()) {
            throw new BusinessException(ResultCode.COUPON_ALREADY_ACQUIRED);
        }

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0);
        userCoupon.setGetTime(LocalDateTime.now());
        return save(userCoupon);
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean useCoupon(Long userId, Long userCouponId, Long orderId) {
        return baseMapper.updateStatusByUser(userId, userCouponId, 1, orderId) > 0;
    }

    @Override
    @Cacheable(key = "'user:' + #userId + ':valid'")
    public List<UserCoupon> getValidCoupons(Long userId) {
        return lambdaQuery()
                .eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getStatus, 0)
                .orderByDesc(UserCoupon::getGetTime)
                .list();
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨3点执行
    @Transactional
    @CacheEvict(allEntries = true)
    public int autoExpireCoupons() {
        return baseMapper.autoExpireCoupons();
    }

    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean returnCoupon(Long userId, Long userCouponId) {
        return baseMapper.updateStatusByUser(userId, userCouponId, 0, null) > 0;
    }
}




