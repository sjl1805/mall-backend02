package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Coupon;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 优惠券Mapper接口
 */
@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 分页查询优惠券（支持多条件筛选）
     *
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页优惠券列表
     */
    IPage<Coupon> selectCouponPage(Page<Coupon> page, @Param("params") Map<String, Object> params);

    /**
     * 根据优惠券类型查询优惠券
     *
     * @param type 优惠券类型：1-满减 2-折扣 3-无门槛
     * @return 优惠券列表
     */
    List<Coupon> selectListByType(Integer type);

    /**
     * 查询可用优惠券（未过期且有库存）
     *
     * @return 优惠券列表
     */
    List<Coupon> selectAvailableCoupons();

    /**
     * 查询即将过期的优惠券
     *
     * @param days 剩余天数
     * @return 优惠券列表
     */
    List<Coupon> selectExpiringCoupons(@Param("days") Integer days);

    /**
     * 查询指定商品可用的优惠券
     *
     * @param productId 商品ID
     * @param categoryId 分类ID
     * @param price 商品价格
     * @return 优惠券列表
     */
    List<Coupon> selectCouponsByProduct(
            @Param("productId") Long productId, 
            @Param("categoryId") Long categoryId, 
            @Param("price") BigDecimal price);

    /**
     * 更新优惠券剩余数量
     *
     * @param couponId 优惠券ID
     * @param count 变更数量（正数增加，负数减少）
     * @return 影响行数
     */
    int updateRemainCount(@Param("couponId") Long couponId, @Param("count") Integer count);

    /**
     * 批量更新优惠券状态
     *
     * @param ids 优惠券ID列表
     * @param status 状态：0-禁用 1-启用
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 统计各类型优惠券数量
     *
     * @return 统计结果
     */
    @MapKey("type")
    List<Map<String, Object>> countCouponByType();

    /**
     * 统计优惠券使用情况
     *
     * @return 统计结果
     */
    @MapKey("type")
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
    @MapKey("couponId")
    List<Map<String, Object>> selectUserAvailableCoupons(
            @Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("categoryId") Long categoryId,
            @Param("totalAmount") BigDecimal totalAmount);

    /**
     * 获取热门优惠券（领取率高的）
     *
     * @param limit 限制数量
     * @return 优惠券列表
     */
    List<Coupon> selectHotCoupons(@Param("limit") Integer limit);
    
    /**
     * 更新优惠券为已过期状态
     *
     * @return 影响行数
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
    List<Coupon> selectBestCoupons(
            @Param("userId") Long userId,
            @Param("totalAmount") BigDecimal totalAmount,
            @Param("productIds") List<Long> productIds,
            @Param("categoryIds") List<Long> categoryIds);
} 