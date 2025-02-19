package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.dto.coupon.CouponPageDTO;
import com.example.model.entity.Coupon;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 优惠券管理Mapper接口
 * 实现优惠券的发放、使用、过期等全生命周期管理
 * 
 * @author 毕业设计学生
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 分页查询优惠券（支持多条件过滤和动态排序）
     * 
     * @param page     分页参数（包含当前页、每页数量）
     * @param queryDTO 查询条件DTO（包含名称、类型、状态等过滤条件）
     * @return 分页结果（包含优惠券列表和分页信息）
     */
    IPage<Coupon> selectCouponPage(IPage<Coupon> page, @Param("query") CouponPageDTO queryDTO);

    /**
     * 更新优惠券状态（同时更新修改时间）
     * 
     * @param couponId 要更新的优惠券ID
     * @param status   新状态（0-失效 1-生效）
     * @return 影响的行数
     */
    int updateStatus(@Param("couponId") Long couponId, @Param("status") Integer status);

    /**
     * 检查优惠券名称唯一性（全局唯一）
     * 
     * @param name      要检查的优惠券名称
     * @param excludeId 需要排除的ID（用于更新操作时排除自身）
     * @return 存在重复返回大于0的值，否则返回0
     */
    int checkNameUnique(@Param("name") String name,
                        @Param("excludeId") Long excludeId);

    /**
     * 自动过期优惠券（定时任务调用）
     * 
     * @param currentTime 当前时间（用于判断过期）
     * @return 过期的优惠券数量
     */
    int expireCoupons(@Param("currentTime") Date currentTime);

    /**
     * 查询当前可用的优惠券（用于前台展示）
     * 
     * @return 可用优惠券列表（按面值倒序排列）
     */
    @Select("SELECT * FROM coupon WHERE status = 1 AND start_time <= NOW() AND end_time >= NOW()")
    List<Coupon> selectAvailableCoupons();

    /**
     * 查询用户有效优惠券（包含领取和使用状态）
     * 
     * @param userId      用户ID
     * @param currentTime 当前时间（用于校验有效期）
     * @return 用户可用的优惠券列表
     */
    List<Coupon> selectValidCouponsByUser(@Param("userId") Long userId,
                                          @Param("currentTime") Date currentTime);
}




