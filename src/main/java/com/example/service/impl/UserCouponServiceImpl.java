package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.UserCouponConstant;
import com.example.mapper.UserCouponMapper;
import com.example.model.entity.Coupon;
import com.example.model.entity.UserCoupon;
import com.example.service.CouponService;
import com.example.service.UserCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户优惠券服务实现类
 */
@Slf4j
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

    @Autowired
    private CouponService couponService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean receiveCoupon(Long userId, Long couponId) {
        // 检查优惠券是否存在且有效
        Coupon coupon = couponService.getById(couponId);
        if (coupon == null) {
            log.error("优惠券不存在: {}", couponId);
            return false;
        }
        
        // 检查优惠券是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            log.error("优惠券不在有效期内: {}", couponId);
            return false;
        }
        
        // 检查优惠券是否已领完
        if (coupon.getQuantity() != null && coupon.getQuantity() > 0) {
            int count = countByCouponId(couponId);
            if (count >= coupon.getQuantity()) {
                log.error("优惠券已领完: {}", couponId);
                return false;
            }
        }
        
        // 检查用户是否已领取过该优惠券
        if (hasReceived(userId, couponId)) {
            log.error("用户已领取过该优惠券: userId={}, couponId={}", userId, couponId);
            return false;
        }
        
        // 检查用户领取次数限制
        if (coupon.getUserLimit() != null && coupon.getUserLimit() > 0) {
            // 这里需要实现检查用户领取次数的逻辑
            // 暂时省略
        }
        
        // 创建用户优惠券记录
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .status(UserCouponConstant.STATUS_UNUSED)
                .createTime(now)
                .updateTime(now)
                .build();
        
        // 保存用户优惠券
        return save(userCoupon);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchGiveCoupon(List<Long> userIds, Long couponId) {
        if (userIds == null || userIds.isEmpty() || couponId == null) {
            return 0;
        }
        
        // 检查优惠券是否存在且有效
        Coupon coupon = couponService.getById(couponId);
        if (coupon == null) {
            log.error("优惠券不存在: {}", couponId);
            return 0;
        }
        
        // 检查优惠券是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            log.error("优惠券不在有效期内: {}", couponId);
            return 0;
        }
        
        // 检查优惠券数量是否足够
        if (coupon.getQuantity() != null && coupon.getQuantity() > 0) {
            int count = countByCouponId(couponId);
            if (count + userIds.size() > coupon.getQuantity()) {
                log.error("优惠券数量不足: {}", couponId);
                return 0;
            }
        }
        
        // 批量创建用户优惠券
        List<UserCoupon> userCouponList = new ArrayList<>();
        for (Long userId : userIds) {
            // 检查用户是否已领取过该优惠券
            if (hasReceived(userId, couponId)) {
                continue;
            }
            
            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(userId)
                    .couponId(couponId)
                    .status(UserCouponConstant.STATUS_UNUSED)
                    .createTime(now)
                    .updateTime(now)
                    .build();
            userCouponList.add(userCoupon);
        }
        
        if (userCouponList.isEmpty()) {
            return 0;
        }
        
        // 批量保存用户优惠券
        saveBatch(userCouponList);
        return userCouponList.size();
    }

    @Override
    public List<UserCoupon> getUserCoupons(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public List<UserCoupon> getUserCouponsByStatus(Long userId, Integer status) {
        return baseMapper.selectByUserIdAndStatus(userId, status);
    }

    @Override
    public IPage<UserCoupon> pageUserCoupons(Long userId, Integer status, long current, long size) {
        Page<UserCoupon> page = new Page<>(current, size);
        return baseMapper.selectPageByUserIdAndStatus(page, userId, status);
    }

    @Override
    public List<UserCoupon> getSoonToExpireCoupons(Long userId, int days) {
        LocalDateTime expireDate = LocalDateTime.now().plusDays(days);
        return baseMapper.selectSoonToExpire(userId, expireDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean useCoupon(Long userCouponId, Long orderId) {
        if (userCouponId == null || orderId == null) {
            return false;
        }
        
        // 查询用户优惠券
        UserCoupon userCoupon = getById(userCouponId);
        if (userCoupon == null) {
            log.error("用户优惠券不存在: {}", userCouponId);
            return false;
        }
        
        // 检查优惠券状态
        if (userCoupon.getStatus() != UserCouponConstant.STATUS_UNUSED) {
            log.error("优惠券状态不可用: {}, status={}", userCouponId, userCoupon.getStatus());
            return false;
        }
        
        // 检查优惠券是否过期
        Coupon coupon = couponService.getById(userCoupon.getCouponId());
        if (coupon == null) {
            log.error("优惠券不存在: {}", userCoupon.getCouponId());
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(coupon.getEndTime())) {
            log.error("优惠券已过期: {}", userCouponId);
            return false;
        }
        
        // 使用优惠券
        int rows = baseMapper.useCoupon(userCouponId, orderId, now);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int returnCouponByOrderId(Long orderId) {
        if (orderId == null) {
            return 0;
        }
        return baseMapper.returnCouponByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int checkAndUpdateExpiredCoupons() {
        LocalDateTime now = LocalDateTime.now();
        return baseMapper.batchUpdateExpired(now);
    }

    @Override
    public Map<String, Integer> countUserCouponByStatus(Long userId) {
        return baseMapper.countUserCouponByStatus(userId);
    }

    @Override
    public int countUserAvailableCoupons(Long userId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getStatus, UserCouponConstant.STATUS_UNUSED);
        return (int) count(queryWrapper);
    }

    @Override
    public boolean hasReceived(Long userId, Long couponId) {
        UserCoupon userCoupon = baseMapper.selectByUserIdAndCouponId(userId, couponId);
        return userCoupon != null;
    }

    @Override
    public int countByCouponId(Long couponId) {
        return baseMapper.countByCouponId(couponId);
    }

    @Override
    public int countByCouponIdAndStatus(Long couponId, Integer status) {
        return baseMapper.countByCouponIdAndStatus(couponId, status);
    }
} 