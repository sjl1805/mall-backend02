package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.UserCoupon;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_coupon(用户优惠券表)】的数据库操作Service
 * @createDate 2025-02-24 12:03:44
 */
public interface UserCouponService extends IService<UserCoupon> {

    /**
     * 根据用户ID查询用户优惠券
     *
     * @param userId 用户ID
     * @return 用户优惠券列表
     */
    List<UserCoupon> selectByUserId(Long userId);

    /**
     * 分页查询用户优惠券
     *
     * @param page 分页信息
     * @return 用户优惠券列表
     */
    IPage<UserCoupon> selectPage(IPage<UserCoupon> page);

    /**
     * 根据ID查询用户优惠券
     *
     * @param id 用户优惠券ID
     * @return 用户优惠券信息
     */
    UserCoupon selectById(Long id);

    /**
     * 新增用户优惠券
     *
     * @param userCoupon 用户优惠券信息
     * @return 插入结果
     */
    boolean insertUserCoupon(UserCoupon userCoupon);

    /**
     * 更新用户优惠券信息
     *
     * @param userCoupon 用户优惠券信息
     * @return 更新结果
     */
    boolean updateUserCoupon(UserCoupon userCoupon);

    /**
     * 根据ID删除用户优惠券
     *
     * @param id 用户优惠券ID
     * @return 删除结果
     */
    boolean deleteUserCoupon(Long id);

    /**
     * 根据用户ID和状态查询用户优惠券
     *
     * @param userId 用户ID
     * @param status 优惠券状态
     * @return 用户优惠券列表
     */
    List<UserCoupon> selectByUserIdAndStatus(Long userId, String status);
}
