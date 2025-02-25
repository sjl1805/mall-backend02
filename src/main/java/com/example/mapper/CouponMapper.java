package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 优惠券数据访问层接口
 */
@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {
    
    /**
     * 获取有效的优惠券列表
     *
     * @return 优惠券列表
     */
    @Select("SELECT * FROM coupon WHERE status = 1 AND remain > 0 AND start_time <= NOW() AND end_time >= NOW() " +
            "ORDER BY threshold ASC")
    List<Coupon> findAvailableCoupons();
    
    /**
     * 获取满足指定金额的优惠券列表
     *
     * @param amount 金额
     * @return 优惠券列表
     */
    @Select("SELECT * FROM coupon WHERE status = 1 AND remain > 0 AND start_time <= NOW() AND end_time >= NOW() " +
            "AND threshold <= #{amount} ORDER BY threshold DESC")
    List<Coupon> findAvailableCouponsByAmount(@Param("amount") BigDecimal amount);
    
    /**
     * 减少优惠券数量
     *
     * @param id 优惠券ID
     * @return 影响行数
     */
    @Update("UPDATE coupon SET remain = remain - 1, update_time = NOW() WHERE id = #{id} AND remain > 0")
    int decreaseRemain(@Param("id") Long id);
} 