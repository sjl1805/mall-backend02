package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:19
 * @Entity model.entity.Coupon
 */
@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 根据优惠券名称模糊查询
     *
     * @param name 优惠券名称
     * @return 优惠券列表
     */
    List<Coupon> selectByNameLike(@Param("name") String name);

    /**
     * 根据优惠券名称查询优惠券
     *
     * @param name 优惠券名称
     * @return 优惠券列表
     */
    List<Coupon> selectByName(@Param("name") String name);

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
    Coupon selectById(@Param("id") Long id);

    /**
     * 插入新优惠券
     *
     * @param coupon 优惠券信息
     * @return 插入结果
     */
    int insertCoupon(Coupon coupon);

    /**
     * 更新优惠券信息
     *
     * @param coupon 优惠券信息
     * @return 更新结果
     */
    int updateCoupon(Coupon coupon);

    /**
     * 根据ID删除优惠券
     *
     * @param id 优惠券ID
     * @return 删除结果
     */
    int deleteCoupon(@Param("id") Long id);

    /**
     * 查询可用优惠券
     *
     * @param now 当前时间
     * @param amount 订单金额
     * @return 可用优惠券列表
     */
    List<Coupon> selectAvailableCoupons(
            @Param("now") Date now,
            @Param("amount") Double amount);

    /**
     * 查询即将过期的优惠券
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 即将过期优惠券列表
     */
    List<Coupon> selectExpiringSoon(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    /**
     * 减少优惠券数量（领取/使用）
     *
     * @param id 优惠券ID
     * @param count 减少数量
     * @return 更新结果
     */
    int decreaseCouponNum(@Param("id") Long id, @Param("count") Integer count);

    /**
     * 检查优惠券是否可用于指定金额
     *
     * @param id 优惠券ID
     * @param amount 订单金额
     * @param now 当前时间
     * @return 可用优惠券信息，无可用返回null
     */
    Coupon checkCouponAvailable(
            @Param("id") Long id,
            @Param("amount") Double amount,
            @Param("now") Date now);

    /**
     * 统计优惠券使用情况
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 优惠券统计数据
     */
    List<Map<String, Object>> getCouponStatistics(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    /**
     * 获取热门优惠券排行
     *
     * @param limit 限制数量
     * @return 热门优惠券列表
     */
    List<Map<String, Object>> getPopularCoupons(@Param("limit") Integer limit);

    /**
     * 批量更新优惠券状态
     *
     * @param ids 优惠券ID列表
     * @param status 新状态
     * @return 更新结果
     */
    int batchUpdateStatus(
            @Param("ids") List<Long> ids,
            @Param("status") Integer status);

    /**
     * 批量删除优惠券
     *
     * @param ids 优惠券ID列表
     * @return 删除结果
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 查询用户未领取的优惠券
     *
     * @param userId 用户ID
     * @param now 当前时间
     * @return 未领取的有效优惠券
     */
    List<Coupon> selectNotReceivedCoupons(
            @Param("userId") Long userId,
            @Param("now") Date now);

    /**
     * 按条件高级查询优惠券
     *
     * @param params 查询参数Map，可包含type,status,minValue,maxValue等
     * @return 优惠券列表
     */
    List<Coupon> advancedSearch(@Param("params") Map<String, Object> params);

    /**
     * 计算优惠券发放效果
     * 
     * @param couponId 优惠券ID
     * @return 统计数据，包含发放量、使用量、使用率、带动销售额等
     */
    Map<String, Object> calculateCouponEffectiveness(@Param("couponId") Long couponId);

    /**
     * 自动失效过期优惠券
     *
     * @param now 当前时间
     * @return 更新的记录数
     */
    int expireCoupons(@Param("now") Date now);
}




