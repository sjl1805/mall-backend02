package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.users.UserCouponPageDTO;
import com.example.model.entity.UserCoupon;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* @author 31815
* @description 针对表【user_coupon(用户优惠券表)】的数据库操作Mapper
* @createDate 2025-02-18 23:43:48
* @Entity model.entity.UserCoupon
*/
public interface UserCouponMapper extends BaseMapper<UserCoupon> {

    /**
     * 分页查询用户优惠券
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<UserCoupon> selectUserCouponPage(IPage<UserCoupon> page, 
                                        @Param("query") UserCouponPageDTO queryDTO);

    /**
     * 更新优惠券使用状态
     * @param userId 用户ID
     * @param userCouponId 用户优惠券ID
     * @param status 新状态
     * @param orderId 关联订单ID
     * @return 影响行数
     */
    @Update("UPDATE user_coupon SET status = #{status}, order_id = #{orderId}, " +
            "use_time = CASE WHEN #{status} = 1 THEN NOW() ELSE use_time END " +
            "WHERE id = #{userCouponId} AND user_id = #{userId}")
    int updateStatusByUser(@Param("userId") Long userId,
                         @Param("userCouponId") Long userCouponId,
                         @Param("status") Integer status,
                         @Param("orderId") Long orderId);

    /**
     * 自动过期优惠券
     * @return 过期数量
     */
    @Update("UPDATE user_coupon SET status = 2 WHERE status = 0 " +
            "AND EXISTS (SELECT 1 FROM coupon c WHERE c.id = coupon_id AND c.end_time < NOW())")
    int autoExpireCoupons();

    /**
     * 检查优惠券有效性
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 有效记录数
     */
    int checkCouponValid(@Param("userId") Long userId,
                        @Param("couponId") Long couponId);
}




