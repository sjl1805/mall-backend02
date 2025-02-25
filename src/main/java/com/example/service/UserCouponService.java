package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserCoupon;

import java.util.List;
import java.util.Map;

/**
 * 用户优惠券服务接口
 */
public interface UserCouponService extends IService<UserCoupon> {
    
    /**
     * 用户领取优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 是否领取成功
     */
    boolean receiveCoupon(Long userId, Long couponId);
    
    /**
     * 批量发放优惠券给用户
     * @param userIds 用户ID列表
     * @param couponId 优惠券ID
     * @return 发放成功数量
     */
    int batchGiveCoupon(List<Long> userIds, Long couponId);
    
    /**
     * 查询用户优惠券列表
     * @param userId 用户ID
     * @return 优惠券列表
     */
    List<UserCoupon> getUserCoupons(Long userId);
    
    /**
     * 根据状态查询用户优惠券
     * @param userId 用户ID
     * @param status 优惠券状态
     * @return 优惠券列表
     */
    List<UserCoupon> getUserCouponsByStatus(Long userId, Integer status);
    
    /**
     * 分页查询用户优惠券
     * @param userId 用户ID
     * @param status 优惠券状态
     * @param current 当前页
     * @param size 每页大小
     * @return 分页数据
     */
    IPage<UserCoupon> pageUserCoupons(Long userId, Integer status, long current, long size);
    
    /**
     * 查询用户即将过期的优惠券
     * @param userId 用户ID
     * @param days 天数
     * @return 即将过期的优惠券列表
     */
    List<UserCoupon> getSoonToExpireCoupons(Long userId, int days);
    
    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     * @return 是否使用成功
     */
    boolean useCoupon(Long userCouponId, Long orderId);
    
    /**
     * 取消订单时返还优惠券
     * @param orderId 订单ID
     * @return 返还数量
     */
    int returnCouponByOrderId(Long orderId);
    
    /**
     * 检查并更新过期优惠券
     * @return 更新数量
     */
    int checkAndUpdateExpiredCoupons();
    
    /**
     * 统计用户各状态优惠券数量
     * @param userId 用户ID
     * @return 状态统计Map
     */
    Map<String, Integer> countUserCouponByStatus(Long userId);
    
    /**
     * 查询用户可用优惠券数量
     * @param userId 用户ID
     * @return 可用优惠券数量
     */
    int countUserAvailableCoupons(Long userId);
    
    /**
     * 检查用户是否已领取指定优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 是否已领取
     */
    boolean hasReceived(Long userId, Long couponId);
    
    /**
     * 查询优惠券领取数量
     * @param couponId 优惠券ID
     * @return 领取数量
     */
    int countByCouponId(Long couponId);
    
    /**
     * 根据优惠券ID和状态统计数量
     * @param couponId 优惠券ID
     * @param status 状态
     * @return 数量
     */
    int countByCouponIdAndStatus(Long couponId, Integer status);
} 