package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CouponMapper;
import com.example.model.entity.Coupon;
import com.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 优惠券服务实现类
 * 
 * 该类实现了优惠券相关的业务逻辑，包括优惠券的创建、查询、更新和删除等功能。
 * 优惠券是电商系统中重要的营销工具，用于刺激消费、提高转化率和客户忠诚度。
 * 系统支持多种类型的优惠券，如满减券、折扣券、无门槛券等，并可设置使用条件和有效期。
 * 使用了Spring缓存机制对优惠券信息进行缓存，提高查询效率，减轻数据库压力。
 * 使用MyBatis-Plus的ServiceImpl简化数据访问操作。
 *
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Service实现
 * @createDate 2025-02-24 12:04:19
 */
@Service
@CacheConfig(cacheNames = "coupons") // 指定该服务类的缓存名称为"coupons"
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
        implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    /**
     * 根据名称查询优惠券列表
     * 
     * 该方法从缓存或数据库获取指定名称的优惠券，
     * 用于前台展示特定活动的优惠券或后台管理系统查询
     *
     * @param name 优惠券名称
     * @return 优惠券列表
     */
    @Override
    @Cacheable(value = "coupons", key = "#name") // 缓存优惠券数据，提高查询效率
    public List<Coupon> selectByName(String name) {
        return couponMapper.selectByName(name);
    }

    /**
     * 分页查询优惠券数据
     * 
     * 该方法用于后台管理系统分页查看优惠券数据，
     * 支持按类型、状态、有效期等条件筛选，便于管理员全面了解优惠券情况
     *
     * @param page 分页参数
     * @return 优惠券分页数据
     */
    @Override
    public IPage<Coupon> selectPage(IPage<Coupon> page) {
        return couponMapper.selectPage(page);
    }

    /**
     * 根据ID查询优惠券
     * 
     * 该方法从缓存或数据库获取指定ID的优惠券详情，
     * 用于查看优惠券详细信息或验证优惠券有效性
     *
     * @param id 优惠券ID
     * @return 优惠券实体
     */
    @Override
    @Cacheable(value = "coupons", key = "#id") // 缓存优惠券详情，提高查询效率
    public Coupon selectById(Long id) {
        return couponMapper.selectById(id);
    }

    /**
     * 创建优惠券
     * 
     * 该方法用于后台管理系统创建新的优惠券，
     * 设置优惠券名称、类型、面值、使用条件、有效期等属性，
     * 是优惠券营销活动的基础操作
     *
     * @param coupon 优惠券实体
     * @return 创建成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "coupons", key = "#coupon.id") // 清除优惠券缓存
    public boolean insertCoupon(Coupon coupon) {
        return couponMapper.insert(coupon) > 0;
    }

    /**
     * 更新优惠券
     * 
     * 该方法用于修改优惠券信息，如调整面值、使用条件、有效期等，
     * 用于优化营销策略或修正错误信息，
     * 并清除相关缓存，确保数据一致性
     *
     * @param coupon 优惠券实体
     * @return 更新成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "coupons", key = "#coupon.id") // 清除优惠券缓存
    public boolean updateCoupon(Coupon coupon) {
        return couponMapper.updateById(coupon) > 0;
    }

    /**
     * 删除优惠券
     * 
     * 该方法用于删除不再需要的优惠券，
     * 通常在营销活动结束后使用，
     * 需要注意的是，已发放给用户的优惠券可能需要特殊处理，
     * 并清除相关缓存
     *
     * @param id 优惠券ID
     * @return 删除成功返回true，失败返回false
     */
    @Override
    @Transactional
    @CacheEvict(value = "coupons", key = "#id") // 清除被删除优惠券的缓存
    public boolean deleteCoupon(Long id) {
        return couponMapper.deleteById(id) > 0;
    }

    /**
     * 设置优惠券有效期
     * 
     * 该方法用于设置或修改优惠券的有效期，
     * 可以灵活调整营销策略，如延长热门优惠券的有效期或缩短错误发放的优惠券有效期，
     * 是优惠券管理的重要功能
     *
     * @param id 优惠券ID
     * @param startTime 有效期开始时间，格式为"yyyy-MM-dd HH:mm:ss"
     * @param endTime 有效期结束时间，格式为"yyyy-MM-dd HH:mm:ss"
     * @return 设置成功返回true，失败返回false
     * @throws RuntimeException 当优惠券不存在时抛出
     */
    @Override
    @Transactional
    public boolean setCouponValidity(Long id, String startTime, String endTime) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        coupon.setStartTime(LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        coupon.setEndTime(LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return couponMapper.updateById(coupon) > 0;
    }

    /**
     * 设置优惠券使用条件
     * 
     * 该方法用于设置或修改优惠券的使用门槛，如最低消费金额，
     * 可以根据不同的营销目标调整使用条件，
     * 比如提高高价值商品的销售或促进特定类目的消费
     *
     * @param id 优惠券ID
     * @param minAmount 最低消费金额
     * @return 设置成功返回true，失败返回false
     * @throws RuntimeException 当优惠券不存在时抛出
     */
    @Override
    @Transactional
    public boolean setCouponConditions(Long id, BigDecimal minAmount) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        coupon.setMinAmount(minAmount);
        return couponMapper.updateById(coupon) > 0;
    }
}



