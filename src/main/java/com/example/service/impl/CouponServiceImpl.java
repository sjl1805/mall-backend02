package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CouponMapper;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:19
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
        implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public List<Coupon> selectByName(String name) {
        return couponMapper.selectByName(name);
    }

    @Override
    public IPage<Coupon> selectPage(IPage<Coupon> page) {
        return couponMapper.selectPage(page);
    }

    @Override
    public Coupon selectById(Long id) {
        return couponMapper.selectById(id);
    }

    @Override
    public boolean insertCoupon(Coupon coupon) {
        return couponMapper.insert(coupon) > 0;
    }

    @Override
    public boolean updateCoupon(Coupon coupon) {
        return couponMapper.updateById(coupon) > 0;
    }

    @Override
    public boolean deleteCoupon(Long id) {
        return couponMapper.deleteById(id) > 0;
    }
}




