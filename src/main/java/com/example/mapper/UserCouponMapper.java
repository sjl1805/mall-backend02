package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.UserCoupon;
import com.example.model.vo.UserCouponVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户优惠券表数据访问层
 *
 * @author 31815
 * @description 针对表【user_coupon(用户优惠券表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:03:44
 */
@Mapper
public interface UserCouponMapper extends BaseMapper<UserCoupon> {

    /**
     * 根据用户ID查询优惠券
     *
     * @param userId 用户ID
     * @return 用户优惠券列表
     */
    List<UserCoupon> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和状态查询用户优惠券
     *
     * @param userId 用户ID
     * @param status 优惠券状态
     * @return 用户优惠券列表
     */
    List<UserCoupon> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 查询用户可用优惠券（未使用且未过期）
     *
     * @param userId      用户ID
     * @param currentTime 当前时间
     * @return 可用优惠券列表
     */
    List<UserCouponVO> selectAvailableCoupons(@Param("userId") Long userId, @Param("currentTime") Date currentTime);

    /**
     * 获取即将过期的优惠券
     *
     * @param userId    用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 即将过期的优惠券
     */
    List<UserCouponVO> selectSoonExpiredCoupons(
            @Param("userId") Long userId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    /**
     * 根据订单金额查询用户可用优惠券
     *
     * @param userId      用户ID
     * @param orderAmount 订单金额
     * @param currentTime 当前时间
     * @return 可用优惠券列表
     */
    List<UserCouponVO> selectCouponsByOrderAmount(
            @Param("userId") Long userId,
            @Param("orderAmount") Double orderAmount,
            @Param("currentTime") Date currentTime);

    /**
     * 更新优惠券使用状态
     *
     * @param id      优惠券ID
     * @param orderId 订单ID
     * @param status  新状态
     * @param useTime 使用时间
     * @return 更新结果
     */
    int updateCouponStatus(
            @Param("id") Long id,
            @Param("orderId") Long orderId,
            @Param("status") Integer status,
            @Param("useTime") Date useTime);

    /**
     * 批量添加用户优惠券
     *
     * @param userId    用户ID
     * @param couponIds 优惠券ID列表
     * @param status    状态
     * @param getTime   获取时间
     * @return 添加结果
     */
    int batchInsertUserCoupons(
            @Param("userId") Long userId,
            @Param("couponIds") List<Long> couponIds,
            @Param("status") Integer status,
            @Param("getTime") Date getTime);

    /**
     * 高级查询用户优惠券
     *
     * @param userId    用户ID
     * @param status    状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      分页参数
     * @return 优惠券列表
     */
    IPage<UserCouponVO> selectUserCouponsByCondition(
            @Param("userId") Long userId,
            @Param("status") Integer status,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            Page<UserCoupon> page);

    /**
     * 插入用户优惠券记录
     *
     * @param couponId 用户优惠券实体
     * @return 插入结果
     */
    int insertUserCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);
}




