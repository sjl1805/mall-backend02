package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.UserCouponDTO;
import com.example.model.entity.UserCoupon;

import java.util.List;

/**
 * 用户优惠券服务接口
 * 
 * @author 31815
 * @description 提供用户优惠券管理功能，包含：
 *              1. 优惠券领取与使用
 *              2. 优惠券状态管理
 *              3. 自动过期处理
 * @createDate 2025-02-18 23:43:48
 */
public interface UserCouponService extends IService<UserCoupon> {

    /**
     * 领取优惠券（带重复校验）
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当已领取时抛出
     */
    boolean acquireCoupon(Long userId, Long couponId);

    /**
     * 使用优惠券（带状态校验）
     * @param userId 用户ID
     * @param userCouponId 用户优惠券ID
     * @param orderId 关联订单ID
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当优惠券不可用时抛出
     */
    boolean useCoupon(Long userId, Long userCouponId, Long orderId);

    /**
     * 获取有效优惠券（带缓存）
     * @param userId 用户ID
     * @return 有效优惠券列表（按领取时间倒序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    List<UserCouponDTO> getValidCoupons(Long userId);

    /**
     * 自动过期优惠券（定时任务）
     * @return 过期数量
     * @implNote 每天凌晨3点自动执行
     */
    int autoExpireCoupons();

    /**
     * 退还优惠券（订单取消时）
     * @param userId 用户ID
     * @param userCouponId 用户优惠券ID
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当优惠券不存在时抛出
     */
    boolean returnCoupon(Long userId, Long userCouponId);
}
