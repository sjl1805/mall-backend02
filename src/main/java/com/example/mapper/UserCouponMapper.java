package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * 查询用户优惠券（关联优惠券信息）
     * @param userId 用户ID
     * @param status 优惠券状态（可选）
     * @return 用户优惠券列表（包含优惠券详细信息）
     */
    List<UserCoupon> selectUserCoupons(@Param("userId") Long userId, 
                                      @Param("status") Integer status);
    
    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 影响行数
     */
    int useCoupon(@Param("userCouponId") Long userCouponId,
                @Param("userId") Long userId,
                @Param("orderId") Long orderId);
    
    /**
     * 统计可用优惠券数量
     * @param userId 用户ID
     * @return 可用数量
     */
    int countAvailableCoupons(@Param("userId") Long userId);
    
    /**
     * 批量插入用户优惠券
     * @param userCoupons 用户优惠券列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<UserCoupon> userCoupons);
    
    /**
     * 查询即将过期优惠券（3天内到期）
     * @param userId 用户ID
     * @return 即将过期优惠券列表
     */
    List<UserCoupon> selectExpiringCoupons(@Param("userId") Long userId);
}




