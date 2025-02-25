package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.UserCoupon;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户优惠券Mapper接口
 */
@Mapper
public interface UserCouponMapper extends BaseMapper<UserCoupon> {
    
    /**
     * 根据用户ID查询优惠券列表
     * @param userId 用户ID
     * @return 优惠券列表
     */
    List<UserCoupon> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID和状态查询优惠券
     * @param userId 用户ID
     * @param status 优惠券状态
     * @return 优惠券列表
     */
    List<UserCoupon> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 查询即将过期的优惠券
     * @param userId 用户ID
     * @param expireDate 过期时间
     * @return 即将过期的优惠券列表
     */
    List<UserCoupon> selectSoonToExpire(@Param("userId") Long userId, @Param("expireDate") LocalDateTime expireDate);
    
    /**
     * 查询用户是否已领取指定优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 用户优惠券，不存在则返回null
     */
    UserCoupon selectByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);
    
    /**
     * 更新优惠券状态
     * @param id 用户优惠券ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 使用优惠券
     * @param id 用户优惠券ID
     * @param orderId 订单ID
     * @param useTime 使用时间
     * @return 影响行数
     */
    int useCoupon(@Param("id") Long id, @Param("orderId") Long orderId, @Param("useTime") LocalDateTime useTime);
    
    /**
     * 批量更新优惠券状态为已过期
     * @param currentTime 当前时间
     * @return 影响行数
     */
    int batchUpdateExpired(@Param("currentTime") LocalDateTime currentTime);
    
    /**
     * 查询订单使用的优惠券
     * @param orderId 订单ID
     * @return 优惠券列表
     */
    List<UserCoupon> selectByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 统计用户各状态优惠券数量
     * @param userId 用户ID
     * @return 状态统计Map
     */
    @MapKey("status")
    Map<String, Integer> countUserCouponByStatus(@Param("userId") Long userId);
    
    /**
     * 分页查询用户优惠券
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 优惠券状态
     * @return 分页数据
     */
    IPage<UserCoupon> selectPageByUserIdAndStatus(
        Page<UserCoupon> page, 
        @Param("userId") Long userId, 
        @Param("status") Integer status
    );
    
    /**
     * 根据优惠券ID查询领取用户
     * @param couponId 优惠券ID
     * @return 用户优惠券列表
     */
    List<UserCoupon> selectUsersByCouponId(@Param("couponId") Long couponId);
    
    /**
     * 取消订单时返还优惠券
     * @param orderId 订单ID
     * @return 影响行数
     */
    int returnCouponByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 批量插入用户优惠券
     * @param userCouponList 用户优惠券列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<UserCoupon> userCouponList);
    
    /**
     * 查询优惠券领取数量
     * @param couponId 优惠券ID
     * @return 领取数量
     */
    int countByCouponId(@Param("couponId") Long couponId);
    
    /**
     * 根据优惠券ID和状态统计数量
     * @param couponId 优惠券ID
     * @param status 状态
     * @return 数量
     */
    int countByCouponIdAndStatus(@Param("couponId") Long couponId, @Param("status") Integer status);
} 