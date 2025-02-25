package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.constants.CouponConstants;
import com.example.exception.BusinessException;
import com.example.mapper.CouponMapper;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 优惠券服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    private final CouponMapper couponMapper;

    @Override
    public IPage<Coupon> getCouponPage(Page<Coupon> page, Map<String, Object> params) {
        return couponMapper.selectCouponPage(page, params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Coupon createCoupon(Coupon coupon) {
        // 验证优惠券信息
        validateCoupon(coupon);

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        coupon.setCreateTime(now);
        coupon.setUpdateTime(now);
        
        // 设置剩余数量等于总数量
        coupon.setRemain(coupon.getTotal());

        // 保存优惠券
        boolean success = save(coupon);
        if (!success) {
            throw new BusinessException(ResultCode.FAILED, "创建优惠券失败");
        }

        return coupon;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCoupon(Coupon coupon) {
        // 验证优惠券ID
        if (coupon.getId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券ID不能为空");
        }

        // 检查优惠券是否存在
        Coupon existingCoupon = getById(coupon.getId());
        if (existingCoupon == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券不存在");
        }

        // 如果更新了总数量，同步更新剩余数量
        if (coupon.getTotal() != null && !coupon.getTotal().equals(existingCoupon.getTotal())) {
            int diff = coupon.getTotal() - existingCoupon.getTotal();
            coupon.setRemain(existingCoupon.getRemain() + diff);
        }

        // 设置更新时间
        coupon.setUpdateTime(LocalDateTime.now());

        return updateById(coupon);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCoupon(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券ID不能为空");
        }

        // 检查优惠券是否存在
        Coupon coupon = getById(id);
        if (coupon == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券不存在");
        }

        // 删除优惠券
        return removeById(id);
    }

    @Override
    public List<Coupon> getListByType(Integer type) {
        if (type == null || type < 1 || type > 3) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券类型无效");
        }
        return couponMapper.selectListByType(type);
    }

    @Override
    public List<Coupon> getAvailableCoupons() {
        return couponMapper.selectAvailableCoupons();
    }

    @Override
    public List<Coupon> getExpiringCoupons(Integer days) {
        if (days == null || days <= 0) {
            days = CouponConstants.DEFAULT_EXPIRING_DAYS; // 默认3天
        }
        return couponMapper.selectExpiringCoupons(days);
    }

    @Override
    public List<Coupon> getCouponsByProduct(Long productId, Long categoryId, BigDecimal price) {
        if (productId == null && categoryId == null) {
            return Collections.emptyList();
        }
        return couponMapper.selectCouponsByProduct(productId, categoryId, price);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRemainCount(Long couponId, Integer count) {
        if (couponId == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券ID不能为空");
        }
        if (count == 0) {
            return true; // 数量未变更，直接返回成功
        }

        // 获取优惠券
        Coupon coupon = getById(couponId);
        if (coupon == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券不存在");
        }

        // 计算新的剩余数量
        int newRemain = coupon.getRemain() + count;
        if (newRemain < 0) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券剩余数量不足");
        }
        if (newRemain > coupon.getTotal()) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券剩余数量不能超过总数量");
        }

        int rows = couponMapper.updateRemainCount(couponId, count);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券状态无效");
        }

        int rows = couponMapper.batchUpdateStatus(ids, status);
        return rows > 0;
    }

    @Override
    public List<Map<String, Object>> countCouponByType() {
        return couponMapper.countCouponByType();
    }

    @Override
    public List<Map<String, Object>> countCouponUsage() {
        return couponMapper.countCouponUsage();
    }

    @Override
    public List<Map<String, Object>> getUserAvailableCoupons(
            Long userId, Long productId, Long categoryId, BigDecimal totalAmount) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return couponMapper.selectUserAvailableCoupons(userId, productId, categoryId, totalAmount);
    }

    @Override
    public List<Coupon> getHotCoupons(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = CouponConstants.DEFAULT_HOT_COUPON_LIMIT; // 默认10张
        }
        return couponMapper.selectHotCoupons(limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateExpiredCoupons() {
        return couponMapper.updateExpiredCoupons();
    }

    @Override
    public List<Coupon> getBestCoupons(
            Long userId, BigDecimal totalAmount, List<Long> productIds, List<Long> categoryIds) {
        if (userId == null || totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Collections.emptyList();
        }
        return couponMapper.selectBestCoupons(userId, totalAmount, productIds, categoryIds);
    }
    
    @Override
    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal totalAmount) {
        if (coupon == null || totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        // 检查是否满足使用门槛
        if (coupon.getThreshold().compareTo(BigDecimal.ZERO) > 0 
                && totalAmount.compareTo(coupon.getThreshold()) < 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discount;
        switch (coupon.getType()) {
            case CouponConstants.TYPE_FULL_REDUCTION: // 满减券
                discount = coupon.getAmount();
                break;
            case CouponConstants.TYPE_DISCOUNT: // 折扣券
                // 折扣券金额表示折扣率，例如8.5表示85折
                discount = totalAmount.multiply(BigDecimal.ONE.subtract(coupon.getAmount().divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP)));
                break;
            case CouponConstants.TYPE_NO_THRESHOLD: // 无门槛券
                discount = coupon.getAmount();
                break;
            default:
                discount = BigDecimal.ZERO;
        }
        
        // 折扣不能超过订单金额
        return discount.min(totalAmount);
    }
    
    @Override
    public boolean isCouponAvailableForProduct(Coupon coupon, Long productId, Long categoryId, BigDecimal price) {
        if (coupon == null) {
            return false;
        }
        
        // 检查优惠券状态
        if (coupon.getStatus() != 1) {
            return false;
        }
        
        // 检查剩余数量
        if (coupon.getRemain() <= 0) {
            return false;
        }
        
        // 检查有效期
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            return false;
        }
        
        // 检查金额门槛
        if (price != null && coupon.getThreshold().compareTo(BigDecimal.ZERO) > 0 
                && price.compareTo(coupon.getThreshold()) < 0) {
            return false;
        }
        
        // 检查分类限制
        List<Long> categoryLimit = coupon.getCategoryLimit();
        if (categoryId != null && categoryLimit != null && !categoryLimit.isEmpty() 
                && !categoryLimit.contains(categoryId)) {
            return false;
        }
        
        // 检查商品限制
        List<Long> productLimit = coupon.getProductLimit();
        if (productId != null && productLimit != null && !productLimit.isEmpty() 
                && !productLimit.contains(productId)) {
            return false;
        }
        
        return true;
    }

    /**
     * 验证优惠券信息
     *
     * @param coupon 优惠券信息
     */
    private void validateCoupon(Coupon coupon) {
        if (coupon == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券信息不能为空");
        }

        if (!StringUtils.hasText(coupon.getName())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券名称不能为空");
        }

        if (coupon.getType() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券类型不能为空");
        }

        if (coupon.getType() < 1 || coupon.getType() > 3) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券类型无效");
        }

        if (coupon.getAmount() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠金额/折扣率不能为空");
        }

        if (coupon.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠金额/折扣率必须大于0");
        }

        // 折扣券折扣率不能超过10（即100%）
        if (coupon.getType() == CouponConstants.TYPE_DISCOUNT && coupon.getAmount().compareTo(BigDecimal.TEN) > 0) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "折扣率不能超过10");
        }

        if (coupon.getThreshold() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "使用门槛金额不能为空");
        }

        if (coupon.getTotal() == null || coupon.getTotal() <= 0) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "优惠券总数量必须大于0");
        }

        if (coupon.getStartTime() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "开始时间不能为空");
        }

        if (coupon.getEndTime() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "结束时间不能为空");
        }

        if (coupon.getEndTime().isBefore(coupon.getStartTime())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "结束时间不能早于开始时间");
        }

        if (coupon.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "结束时间不能早于当前时间");
        }
    }
} 