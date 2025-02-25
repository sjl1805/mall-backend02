package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserCoupon;
import com.example.model.entity.Coupon;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户优惠券服务接口
 */
public interface UserCouponService extends IService<UserCoupon> {
    
    /**
     * 获取用户优惠券列表
     *
     * @param userId 用户ID
     * @param status 优惠券状态：0-未使用 1-已使用 2-已过期
     * @return 用户优惠券列表
     */
    List<UserCoupon> getUserCoupons(Long userId, Integer status);
    
    /**
     * 获取用户可用优惠券列表
     *
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    List<UserCoupon> getAvailableCoupons(Long userId);
    
    /**
     * 获取用户可用的且满足订单金额的优惠券列表
     *
     * @param userId 用户ID
     * @param amount 订单金额
     * @return 可用优惠券列表
     */
    List<UserCoupon> getAvailableCouponsByAmount(Long userId, BigDecimal amount);
    
    /**
     * 领取优惠券
     *
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 领取的用户优惠券
     */
    UserCoupon receiveCoupon(Long userId, Long couponId);
    
    /**
     * 使用优惠券
     *
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     * @return 是否使用成功
     */
    boolean useCoupon(Long userCouponId, Long orderId);
    
    /**
     * 取消使用优惠券
     *
     * @param orderId 订单ID
     * @return 是否取消成功
     */
    boolean cancelUseCoupon(Long orderId);
    
    /**
     * 检查用户是否已领取该优惠券
     *
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 是否已领取
     */
    boolean hasCoupon(Long userId, Long couponId);

    /**
     * 获取用户优惠券
     *
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 用户优惠券
     */
    UserCoupon getUserCoupon(Long userId, Long couponId);

    /**
     * 获取优惠券信息
     *
     * @param couponId 优惠券ID
     * @return 优惠券信息
     */
    Coupon getCouponInfo(Long couponId);
} 