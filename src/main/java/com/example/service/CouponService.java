package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Coupon;
import java.util.List;
import java.math.BigDecimal;
/**
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Service
 * @createDate 2025-02-24 12:04:19
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 根据优惠券名称查询优惠券
     *
     * @param name 优惠券名称
     * @return 优惠券列表
     */
    List<Coupon> selectByName(String name);

    /**
     * 分页查询优惠券
     *
     * @param page 分页信息
     * @return 优惠券列表
     */
    IPage<Coupon> selectPage(IPage<Coupon> page);

    /**
     * 根据ID查询优惠券
     *
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    Coupon selectById(Long id);

    /**
     * 新增优惠券
     *
     * @param coupon 优惠券信息
     * @return 插入结果
     */
    boolean insertCoupon(Coupon coupon);

    /**
     * 更新优惠券信息
     *
     * @param coupon 优惠券信息
     * @return 更新结果
     */
    boolean updateCoupon(Coupon coupon);

    /**
     * 根据ID删除优惠券
     *
     * @param id 优惠券ID
     * @return 删除结果
     */
    boolean deleteCoupon(Long id);
  
    /**
     * 设置优惠券有效期
     *
     * @param id 优惠券ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    boolean setCouponValidity(Long id, String startTime, String endTime);

    /**
     * 设置优惠券使用条件
     *
     * @param id 优惠券ID
     * @param minAmount 最小金额    
     */
    boolean setCouponConditions(Long id, BigDecimal minAmount);
}