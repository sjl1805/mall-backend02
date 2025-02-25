package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Coupon;

import java.math.BigDecimal;
import java.util.List;

/**
 * 优惠券服务接口
 */
public interface CouponService extends IService<Coupon> {
    
    /**
     * 获取可用优惠券列表
     *
     * @return 优惠券列表
     */
    List<Coupon> getAvailableCoupons();
    
    /**
     * 获取满足指定金额的优惠券列表
     *
     * @param amount 订单金额
     * @return 可用优惠券列表
     */
    List<Coupon> getAvailableCouponsByAmount(BigDecimal amount);
    
    /**
     * 分页查询优惠券
     *
     * @param page 分页参数
     * @return 分页优惠券列表
     */
    IPage<Coupon> pageCoupons(Page<Coupon> page);
    
    /**
     * 添加优惠券
     *
     * @param coupon 优惠券信息
     * @return 添加成功的优惠券
     */
    Coupon addCoupon(Coupon coupon);
    
    /**
     * 更新优惠券
     *
     * @param coupon 优惠券信息
     * @return 是否更新成功
     */
    boolean updateCoupon(Coupon coupon);
    
    /**
     * 减少优惠券数量
     *
     * @param couponId 优惠券ID
     * @return 是否减少成功
     */
    boolean decreaseCouponRemain(Long couponId);
    
    /**
     * 更新优惠券状态
     *
     * @param couponId 优惠券ID
     * @param status 状态：0-禁用 1-启用
     * @return 是否更新成功
     */
    boolean updateCouponStatus(Long couponId, Integer status);
} 