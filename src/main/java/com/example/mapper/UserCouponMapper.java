package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.users.UserCouponPageDTO;
import com.example.model.entity.UserCoupon;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户优惠券管理Mapper接口
 * 实现优惠券的领取、使用和状态管理
 * 
 * @author 毕业设计学生
 */
public interface UserCouponMapper extends BaseMapper<UserCoupon> {

    /**
     * 分页查询用户优惠券（支持多条件过滤）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含用户ID、状态等）
     * @return 分页结果（包含优惠券列表和分页信息）
     */
    IPage<UserCoupon> selectUserCouponPage(IPage<UserCoupon> page,
                                         @Param("query") UserCouponPageDTO queryDTO);

    /**
     * 用户使用优惠券（事务操作）
     * 
     * @param userId       用户ID（必填）
     * @param userCouponId 用户优惠券ID（必填）
     * @param status       新状态（1-已使用 2-已过期）
     * @param orderId      关联订单ID（使用必填）
     * @return 影响的行数（0表示操作失败）
     */
    @Update("UPDATE user_coupon SET status = #{status}, order_id = #{orderId}, " +
            "use_time = CASE WHEN #{status} = 1 THEN NOW() ELSE use_time END " +
            "WHERE id = #{userCouponId} AND user_id = #{userId}")
    int updateStatusByUser(@Param("userId") Long userId,
                          @Param("userCouponId") Long userCouponId,
                          @Param("status") Integer status,
                          @Param("orderId") Long orderId);

    /**
     * 自动过期优惠券（定时任务调用）
     * 
     * @return 成功过期的记录数
     */
    @Update("UPDATE user_coupon SET status = 2 WHERE status = 0 " +
            "AND EXISTS (SELECT 1 FROM coupon c WHERE c.id = coupon_id AND c.end_time < NOW())")
    int autoExpireCoupons();

    /**
     * 检查优惠券有效性（下单时校验）
     * 
     * @param userId   用户ID（必填）
     * @param couponId 优惠券ID（必填）
     * @return 有效返回1，否则返回0
     */
    int checkCouponValid(@Param("userId") Long userId,
                        @Param("couponId") Long couponId);
}




