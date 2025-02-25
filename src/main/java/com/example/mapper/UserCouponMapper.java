package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户优惠券数据访问层接口
 */
@Mapper
public interface UserCouponMapper extends BaseMapper<UserCoupon> {
    
    /**
     * 获取用户的优惠券列表
     *
     * @param userId 用户ID
     * @param status 优惠券状态
     * @return 用户优惠券列表
     */
    @Select("SELECT * FROM user_coupon WHERE user_id = #{userId} AND status = #{status} ORDER BY get_time DESC")
    List<UserCoupon> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 获取用户的可用优惠券列表
     *
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    @Select("SELECT uc.* FROM user_coupon uc " +
            "JOIN coupon c ON uc.coupon_id = c.id " +
            "WHERE uc.user_id = #{userId} AND uc.status = 0 " +
            "AND c.start_time <= NOW() AND c.end_time >= NOW() " +
            "ORDER BY c.threshold DESC")
    List<UserCoupon> findAvailableByUserId(@Param("userId") Long userId);
    
    /**
     * 使用优惠券
     *
     * @param id 用户优惠券ID
     * @param orderId 订单ID
     * @return 影响行数
     */
    @Update("UPDATE user_coupon SET status = 1, order_id = #{orderId}, use_time = NOW(), update_time = NOW() " +
            "WHERE id = #{id} AND status = 0")
    int useCoupon(@Param("id") Long id, @Param("orderId") Long orderId);
    
    /**
     * 取消使用优惠券
     *
     * @param orderId 订单ID
     * @return 影响行数
     */
    @Update("UPDATE user_coupon SET status = 0, order_id = NULL, use_time = NULL, update_time = NOW() " +
            "WHERE order_id = #{orderId} AND status = 1")
    int cancelUseCoupon(@Param("orderId") Long orderId);
    
    /**
     * 检查用户是否已领取该优惠券
     *
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 用户优惠券数量
     */
    @Select("SELECT COUNT(*) FROM user_coupon WHERE user_id = #{userId} AND coupon_id = #{couponId}")
    int countByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);
} 