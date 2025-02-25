package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exception.BusinessException;
import com.example.mapper.CouponMapper;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券服务实现类
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Override
    public List<Coupon> getAvailableCoupons() {
        return baseMapper.findAvailableCoupons();
    }

    @Override
    public List<Coupon> getAvailableCouponsByAmount(BigDecimal amount) {
        return baseMapper.findAvailableCouponsByAmount(amount);
    }

    @Override
    public IPage<Coupon> pageCoupons(Page<Coupon> page) {
        LambdaQueryWrapper<Coupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Coupon::getCreateTime);
        return page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Coupon addCoupon(Coupon coupon) {
        // 校验优惠券参数
        if (coupon.getThreshold() == null || coupon.getThreshold().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("优惠券门槛金额不能为负数");
        }
        
        if (coupon.getDiscount() == null || coupon.getDiscount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("优惠券折扣金额必须大于0");
        }
        
        if (coupon.getStartTime() == null || coupon.getEndTime() == null) {
            throw new BusinessException("优惠券有效期不能为空");
        }
        
        if (coupon.getStartTime().isAfter(coupon.getEndTime())) {
            throw new BusinessException("优惠券开始时间不能晚于结束时间");
        }
        
        // 设置默认值
        coupon.setStatus(coupon.getStatus() == null ? 1 : coupon.getStatus());
        coupon.setRemain(coupon.getTotal());
        coupon.setCreateTime(LocalDateTime.now());
        coupon.setUpdateTime(LocalDateTime.now());
        
        // 保存优惠券
        save(coupon);
        
        return coupon;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCoupon(Coupon coupon) {
        if (coupon == null || coupon.getId() == null) {
            throw new BusinessException("优惠券ID不能为空");
        }
        
        // 获取原优惠券
        Coupon original = getById(coupon.getId());
        if (original == null) {
            throw new BusinessException("优惠券不存在");
        }
        
        // 校验是否允许修改
        if (original.getStatus() == 1 && LocalDateTime.now().isAfter(original.getStartTime())) {
            throw new BusinessException("优惠券已生效，不能修改");
        }
        
        // 校验参数
        if (coupon.getThreshold() != null && coupon.getThreshold().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("优惠券门槛金额不能为负数");
        }
        
        if (coupon.getDiscount() != null && coupon.getDiscount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("优惠券折扣金额必须大于0");
        }
        
        // 如果修改了总数，同步修改剩余数量
        if (coupon.getTotal() != null && !coupon.getTotal().equals(original.getTotal())) {
            int diff = coupon.getTotal() - original.getTotal();
            coupon.setRemain(original.getRemain() + diff);
        }
        
        // 不允许修改的字段设为null
        coupon.setCreateTime(null);
        
        coupon.setUpdateTime(LocalDateTime.now());
        
        return updateById(coupon);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseCouponRemain(Long couponId) {
        return baseMapper.decreaseRemain(couponId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCouponStatus(Long couponId, Integer status) {
        if (status != 0 && status != 1) {
            throw new BusinessException("状态参数错误");
        }
        
        LambdaUpdateWrapper<Coupon> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Coupon::getId, couponId)
                .set(Coupon::getStatus, status)
                .set(Coupon::getUpdateTime, LocalDateTime.now());
        
        return update(updateWrapper);
    }
} 