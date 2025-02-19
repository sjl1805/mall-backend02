package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.CouponMapper;
import com.example.model.dto.coupon.CouponDTO;
import com.example.model.dto.coupon.CouponPageDTO;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Service实现
 * @createDate 2025-02-18 23:44:26
 */
@Service
@CacheConfig(cacheNames = "couponService")
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
        implements CouponService {

    //private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean createCoupon(CouponDTO couponDTO) {
        if (baseMapper.checkNameUnique(couponDTO.getName(), null) > 0) {
            throw new BusinessException(ResultCode.COUPON_NAME_EXISTS);
        }

        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDTO, coupon);
        return save(coupon);
    }

    @Override
    @Cacheable(key = "'page:' + #queryDTO.hashCode()")
    public IPage<Coupon> listCouponPage(CouponPageDTO queryDTO) {
        Page<Coupon> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return baseMapper.selectCouponPage(page, queryDTO);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean updateStatus(Long couponId, Integer status) {
        return baseMapper.updateStatus(couponId, status) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean expireCoupons() {
        return baseMapper.expireCoupons(new Date()) > 0;
    }

    @Override
    @Cacheable(key = "'available'")
    public List<Coupon> getAvailableCoupons() {
        return baseMapper.selectAvailableCoupons();
    }

    @Override
    @Cacheable(key = "'user:' + #userId")
    public List<Coupon> getUserValidCoupons(Long userId) {
        return baseMapper.selectValidCouponsByUser(userId, new Date());
    }
}




