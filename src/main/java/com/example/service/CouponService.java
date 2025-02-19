package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.coupon.CouponDTO;
import com.example.model.dto.coupon.CouponPageDTO;
import com.example.model.entity.Coupon;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Service
 * @createDate 2025-02-18 23:44:26
 */
public interface CouponService extends IService<Coupon> {
    boolean createCoupon(CouponDTO couponDTO);

    IPage<Coupon> listCouponPage(CouponPageDTO queryDTO);

    boolean updateStatus(Long couponId, Integer status);

    boolean expireCoupons();

    List<Coupon> getAvailableCoupons();

    List<Coupon> getUserValidCoupons(Long userId);
}
