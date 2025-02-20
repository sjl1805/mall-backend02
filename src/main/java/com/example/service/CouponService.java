package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.coupon.CouponDTO;
import com.example.model.dto.coupon.CouponPageDTO;
import com.example.model.entity.Coupon;

import java.util.List;

/**
 * 优惠券服务接口
 * 
 * @author 31815
 * @description 提供优惠券全生命周期管理功能，包含：
 *              1. 优惠券的创建与发放
 *              2. 状态管理（启用/禁用）
 *              3. 自动过期处理
 *              4. 用户可用券查询
 * @createDate 2025-02-18 23:44:26
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 创建优惠券（管理端）
     * @param couponDTO 优惠券数据传输对象，包含：
     *                  - name: 券名称（唯一）
     *                  - type: 券类型（1-满减券，2-折扣券）
     *                  - rule: 优惠规则（JSON格式）
     *                  - validDays: 领取后有效天数
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当券名称重复时抛出COUPON_NAME_EXISTS
     */
    boolean createCoupon(CouponDTO couponDTO);

    /**
     * 分页查询优惠券（管理端）
     * @param queryDTO 分页查询参数：
     *                 - page: 当前页码
     *                 - size: 每页条数
     *                 - status: 状态过滤（0-禁用，1-启用）
     *                 - name: 券名称模糊查询
     * @return 分页结果（包含券基本信息及状态）
     */
    IPage<CouponDTO> listCouponPage(CouponPageDTO queryDTO);

    /**
     * 更新优惠券状态（启用/禁用）
     * @param couponId 目标券ID
     * @param status 新状态（1-启用，0-禁用）
     * @return 操作是否成功
     * @throws com.example.exception.BusinessException 当券不存在时抛出COUPON_NOT_FOUND
     */
    boolean updateStatus(Long couponId, Integer status);

    /**
     * 批量过期优惠券（定时任务）
     * @return 过期券数量
     * @implNote 每日凌晨执行，更新过期时间为当前时间且状态为已过期
     */
    boolean expireCoupons();

    /**
     * 获取可领取优惠券列表（用户端）
     * @return 有效且未过期的优惠券列表
     * @implNote 结果缓存优化，有效期5分钟
     */
    List<CouponDTO> getAvailableCoupons();

    /**
     * 获取用户有效优惠券（用户中心）
     * @param userId 用户ID
     * @return 用户未使用且未过期的优惠券列表
     * @implNote 包含券状态校验：
     *           1. 未使用（usageStatus=0）
     *           2. 有效期大于当前时间
     */
    List<CouponDTO> getUserValidCoupons(Long userId);
}
