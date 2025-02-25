package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Coupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 优惠券服务接口
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 分页查询优惠券
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页优惠券列表
     */
    IPage<Coupon> getCouponPage(Page<Coupon> page, Map<String, Object> params);

    /**
     * 创建优惠券
     *
     * @param coupon 优惠券信息
     * @return 创建的优惠券
     */
    Coupon createCoupon(Coupon coupon);

    /**
     * 更新优惠券
     *
     * @param coupon 优惠券信息
     * @return 是否更新成功
     */
    boolean updateCoupon(Coupon coupon);

    /**
     * 删除优惠券
     *
     * @param id 优惠券ID
     * @return 是否删除成功
     */
    boolean deleteCoupon(Long id);

    /**
     * 根据优惠券类型查询优惠券
     *
     * @param type 优惠券类型：1-满减 2-折扣 3-无门槛
     * @return 优惠券列表
     */
    List<Coupon> getListByType(Integer type);

    /**
     * 查询可用优惠券（未过期且有库存）
     *
     * @return 优惠券列表
     */
    List<Coupon> getAvailableCoupons();

    /**
     * 查询即将过期的优惠券
     *
     * @param days 剩余天数
     * @return 优惠券列表
     */
    List<Coupon> getExpiringCoupons(Integer days);

    /**
     * 查询指定商品可用的优惠券
     *
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @param price 商品价格
     * @return 优惠券列表
     */
    List<Coupon> getCouponsByProduct(Long productId, Long categoryId, BigDecimal price);

    /**
     * 更新优惠券剩余数量
     *
     * @param couponId 优惠券ID
     * @param count 变更数量（正数增加，负数减少）
     * @return 是否更新成功
     */
    boolean updateRemainCount(Long couponId, Integer count);

    /**
     * 批量更新优惠券状态
     *
     * @param ids 优惠券ID列表
     * @param status 状态：0-禁用 1-启用
     * @return 是否更新成功
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);

    /**
     * 统计各类型优惠券数量
     *
     * @return 统计结果
     */
    List<Map<String, Object>> countCouponByType();

    /**
     * 统计优惠券使用情况
     *
     * @return 统计结果
     */
    List<Map<String, Object>> countCouponUsage();

    /**
     * 查询用户可用优惠券
     *
     * @param userId 用户ID
     * @param productId 商品ID（可选）
     * @param categoryId 分类ID（可选）
     * @param totalAmount 订单金额（可选）
     * @return 优惠券列表
     */
    List<Map<String, Object>> getUserAvailableCoupons(
            Long userId, Long productId, Long categoryId, BigDecimal totalAmount);

    /**
     * 获取热门优惠券（领取率高的）
     *
     * @param limit 限制数量
     * @return 优惠券列表
     */
    List<Coupon> getHotCoupons(Integer limit);
    
    /**
     * 更新已过期优惠券状态
     *
     * @return 更新数量
     */
    int updateExpiredCoupons();

    /**
     * 获取用户最适合的优惠券
     *
     * @param userId 用户ID
     * @param totalAmount 订单金额
     * @param productIds 商品ID列表
     * @param categoryIds 分类ID列表
     * @return 优惠券列表
     */
    List<Coupon> getBestCoupons(
            Long userId, BigDecimal totalAmount, List<Long> productIds, List<Long> categoryIds);
    
    /**
     * 计算优惠券折扣金额
     *
     * @param coupon 优惠券
     * @param totalAmount 订单总金额
     * @return 折扣金额
     */
    BigDecimal calculateDiscount(Coupon coupon, BigDecimal totalAmount);
    
    /**
     * 判断优惠券是否可用于商品
     *
     * @param coupon 优惠券
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @param price 商品价格
     * @return 是否可用
     */
    boolean isCouponAvailableForProduct(Coupon coupon, Long productId, Long categoryId, BigDecimal price);
} 