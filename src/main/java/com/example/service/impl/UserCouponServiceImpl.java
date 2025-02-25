package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.UserCouponMapper;
import com.example.model.entity.Coupon;
import com.example.model.entity.UserCoupon;
import com.example.service.CouponService;
import com.example.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户优惠券服务实现类
 */
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

    @Autowired
    private CouponService couponService;

    @Override
    public List<UserCoupon> getUserCoupons(Long userId, Integer status) {
        return baseMapper.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<UserCoupon> getAvailableCoupons(Long userId) {
        return baseMapper.findAvailableByUserId(userId);
    }

    @Override
    public List<UserCoupon> getAvailableCouponsByAmount(Long userId, BigDecimal amount) {
        // 获取用户所有可用的优惠券
        List<UserCoupon> availableCoupons = getAvailableCoupons(userId);
        if (availableCoupons.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取优惠券详情并过滤出满足金额要求的优惠券
        List<Long> couponIds = availableCoupons.stream()
                .map(UserCoupon::getCouponId)
                .collect(Collectors.toList());
        
        List<Coupon> coupons = couponService.listByIds(couponIds);
        List<Coupon> validCoupons = coupons.stream()
                .filter(coupon -> amount.compareTo(coupon.getThreshold()) >= 0)
                .collect(Collectors.toList());
        
        List<Long> validCouponIds = validCoupons.stream()
                .map(Coupon::getId)
                .collect(Collectors.toList());
        
        // 过滤出有效的用户优惠券
        return availableCoupons.stream()
                .filter(uc -> validCouponIds.contains(uc.getCouponId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCoupon receiveCoupon(Long userId, Long couponId) {
        // 检查用户是否已领取该优惠券
        if (hasCoupon(userId, couponId)) {
            throw new BusinessException("您已领取过该优惠券");
        }
        
        // 检查优惠券是否有效
        Coupon coupon = couponService.getById(couponId);
        if (coupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        
        if (coupon.getStatus() != 1) {
            throw new BusinessException("优惠券已下架");
        }
        
        if (LocalDateTime.now().isBefore(coupon.getStartTime()) || LocalDateTime.now().isAfter(coupon.getEndTime())) {
            throw new BusinessException("优惠券不在有效期内");
        }
        
        if (coupon.getRemain() <= 0) {
            throw new BusinessException("优惠券已领完");
        }
        
        // 减少优惠券数量
        boolean decreased = couponService.decreaseCouponRemain(couponId);
        if (!decreased) {
            throw new BusinessException("优惠券已领完");
        }
        
        // 创建用户优惠券记录
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0); // 未使用
        userCoupon.setGetTime(LocalDateTime.now());
        userCoupon.setCreateTime(LocalDateTime.now());
        userCoupon.setUpdateTime(LocalDateTime.now());
        
        save(userCoupon);
        
        return userCoupon;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean useCoupon(Long userCouponId, Long orderId) {
        return baseMapper.useCoupon(userCouponId, orderId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelUseCoupon(Long orderId) {
        return baseMapper.cancelUseCoupon(orderId) > 0;
    }

    @Override
    public boolean hasCoupon(Long userId, Long couponId) {
        return baseMapper.countByUserIdAndCouponId(userId, couponId) > 0;
    }

    @Override
    public UserCoupon getUserCoupon(Long userId, Long couponId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getCouponId, couponId)
                .eq(UserCoupon::getStatus, 0); // 未使用
        return getOne(queryWrapper);
    }

    @Override
    public Coupon getCouponInfo(Long couponId) {
        return couponService.getById(couponId);
    }
} 