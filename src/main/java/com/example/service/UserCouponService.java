package com.example.service;

import com.example.model.entity.UserCoupon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_coupon(用户优惠券表)】的数据库操作Service
 * @createDate 2025-02-18 23:43:48
 */
public interface UserCouponService extends IService<UserCoupon> {
    boolean acquireCoupon(Long userId, Long couponId);

    boolean useCoupon(Long userId, Long userCouponId, Long orderId);

    List<UserCoupon> getValidCoupons(Long userId);

    int autoExpireCoupons();

    boolean returnCoupon(Long userId, Long userCouponId);
}
