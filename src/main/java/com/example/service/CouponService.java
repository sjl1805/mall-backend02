package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Coupon;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
     * @param id        优惠券ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    boolean setCouponValidity(Long id, String startTime, String endTime);

    /**
     * 设置优惠券使用条件
     *
     * @param id        优惠券ID
     * @param minAmount 最小金额
     */
    boolean setCouponConditions(Long id, BigDecimal minAmount);
    
    /**
     * 查询可用优惠券
     * 
     * @param amount 订单金额
     * @return 可用优惠券列表
     */
    List<Coupon> selectAvailableCoupons(BigDecimal amount);
    
    /**
     * 查询即将过期的优惠券
     * 
     * @param days 天数，如7天内过期
     * @return 即将过期的优惠券列表
     */
    List<Coupon> selectExpiringSoon(Integer days);
    
    /**
     * 减少优惠券数量（领取时调用）
     * 
     * @param id 优惠券ID
     * @param count 减少数量
     * @return 是否成功
     */
    boolean decreaseCouponNum(Long id, Integer count);
    
    /**
     * 检查优惠券是否可用于指定金额
     * 
     * @param id 优惠券ID
     * @param amount 订单金额
     * @return 可用优惠券，不可用返回null
     */
    Coupon checkCouponAvailable(Long id, BigDecimal amount);
    
    /**
     * 获取优惠券使用统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 优惠券统计数据
     */
    List<Map<String, Object>> getCouponStatistics(Date startDate, Date endDate);
    
    /**
     * 获取热门优惠券排行
     * 
     * @param limit 限制数量
     * @return 热门优惠券列表
     */
    List<Map<String, Object>> getPopularCoupons(Integer limit);
    
    /**
     * 批量更新优惠券状态
     * 
     * @param ids 优惠券ID列表
     * @param status 状态
     * @return 是否成功
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);
    
    /**
     * 批量删除优惠券
     * 
     * @param ids 优惠券ID列表
     * @return 是否成功
     */
    boolean batchDelete(List<Long> ids);
}