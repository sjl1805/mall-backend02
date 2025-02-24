package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CouponMapper;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:19
 */
@Service
@CacheConfig(cacheNames = "coupons")
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
        implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    @Cacheable(value = "coupons", key = "#name")
    public List<Coupon> selectByName(String name) {
        return couponMapper.selectByName(name);
    }

    @Override
    public IPage<Coupon> selectPage(IPage<Coupon> page) {
        return couponMapper.selectPage(page);
    }

    @Override
    @Cacheable(value = "coupons", key = "#id")
    public Coupon selectById(Long id) {
        return couponMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "coupons", key = "#coupon.id")
    public boolean insertCoupon(Coupon coupon) {
        return couponMapper.insert(coupon) > 0;
    }

    @Override
    @CacheEvict(value = "coupons", key = "#coupon.id")
    public boolean updateCoupon(Coupon coupon) {
        return couponMapper.updateById(coupon) > 0;
    }

    @Override
    @CacheEvict(value = "coupons", key = "#id")
    public boolean deleteCoupon(Long id) {
        return couponMapper.deleteById(id) > 0;
    }

    @Override
    public boolean setCouponValidity(Long id, String startTime, String endTime) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        coupon.setStartTime(LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        coupon.setEndTime(LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return couponMapper.updateById(coupon) > 0;
    }

    @Override
    public boolean setCouponConditions(Long id, BigDecimal minAmount) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        coupon.setMinAmount(minAmount);
        return couponMapper.updateById(coupon) > 0;
    }
}



