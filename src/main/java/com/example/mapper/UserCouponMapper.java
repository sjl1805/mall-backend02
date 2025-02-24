package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.UserCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【user_coupon(用户优惠券表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:44
 * @Entity model.entity.UserCoupon
 */
public interface UserCouponMapper extends BaseMapper<UserCoupon> {

    /**
     * 根据用户ID查询优惠券
     *
     * @param userId 用户ID
     * @return 用户优惠券列表
     */
    List<UserCoupon> selectByUserId(@Param("userId") Long userId);

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
    UserCoupon selectById(@Param("id") Long id);

    /**
     * 插入新用户优惠券
     *
     * @param userCoupon 用户优惠券信息
     * @return 插入结果
     */
    int insertUserCoupon(UserCoupon userCoupon);

    /**
     * 更新用户优惠券信息
     *
     * @param userCoupon 用户优惠券信息
     * @return 更新结果
     */
    int updateUserCoupon(UserCoupon userCoupon);

    /**
     * 根据ID删除用户优惠券
     *
     * @param id 用户优惠券ID
     * @return 删除结果
     */
    int deleteUserCoupon(@Param("id") Long id);

    /**
     * 根据用户ID和状态查询用户优惠券
     *
     * @param userId 用户ID
     * @param status 优惠券状态
     * @return 用户优惠券列表
     */
    List<UserCoupon> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}




