package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.exception.BusinessException;
import com.example.mapper.UserCouponMapper;
import com.example.model.entity.UserCoupon;
import com.example.model.dto.users.UserCouponDTO;
import com.example.service.UserCouponService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户优惠券服务实现类
 * 
 * @author 31815
 * @description 实现用户优惠券核心业务逻辑，包含：
 *              1. 领取限制校验
 *              2. 状态变更控制
 *              3. 缓存策略管理
 * @createDate 2025-02-18 23:43:48
 */
@Service
@CacheConfig(cacheNames = "userCouponService")
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon>
        implements UserCouponService {

    /**
     * 领取优惠券（完整校验）
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 操作结果
     * @implNote 业务逻辑：
     *           1. 校验是否重复领取
     *           2. 创建优惠券记录
     *           3. 清除用户缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean acquireCoupon(Long userId, Long couponId) {
        if (lambdaQuery()
                .eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getCouponId, couponId)
                .exists()) {
            throw new BusinessException(ResultCode.COUPON_ALREADY_ACQUIRED);
        }

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0);
        userCoupon.setGetTime(LocalDateTime.now());
        return save(userCoupon);
    }

    /**
     * 使用优惠券（原子操作）
     * @param userId 用户ID
     * @param userCouponId 优惠券ID
     * @param orderId 订单ID
     * @return 操作结果
     * @implNote 使用数据库行锁保证原子性
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean useCoupon(Long userId, Long userCouponId, Long orderId) {
        return baseMapper.updateStatusByUser(userId, userCouponId, 1, orderId) > 0;
    }

    /**
     * 获取有效优惠券（缓存优化）
     * @param userId 用户ID
     * @return 优惠券列表
     * @implNote 缓存策略：
     *           1. 缓存键：user:{userId}:valid
     *           2. 缓存时间：30分钟
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':valid'")
    public List<UserCouponDTO> getValidCoupons(Long userId) {
        return lambdaQuery()
                .eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getStatus, 0)
                .orderByDesc(UserCoupon::getGetTime)
                .list()
                .stream()
                .map(UserCouponDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 自动过期优惠券（定时任务）
     * @return 过期数量
     * @implNote 执行逻辑：
     *           1. 扫描过期优惠券
     *           2. 更新状态为已过期
     *           3. 清除全量缓存
     */
    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    @CacheEvict(allEntries = true)
    public int autoExpireCoupons() {
        return baseMapper.autoExpireCoupons();
    }

    /**
     * 退还优惠券（安全操作）
     * @param userId 用户ID
     * @param userCouponId 优惠券ID
     * @return 操作结果
     * @implNote 执行逻辑：
     *           1. 校验优惠券归属
     *           2. 更新状态为未使用
     *           3. 清除用户缓存
     */
    @Override
    @Transactional
    @CacheEvict(key = "'user:' + #userId")
    public boolean returnCoupon(Long userId, Long userCouponId) {
        return baseMapper.updateStatusByUser(userId, userCouponId, 0, null) > 0;
    }

}




