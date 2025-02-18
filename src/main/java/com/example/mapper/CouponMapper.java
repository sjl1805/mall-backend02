package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Coupon;
import com.example.model.query.CouponQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 优惠券管理Mapper接口
 * @author 毕业设计学生
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 分页查询优惠券列表（带条件）
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<Coupon> selectCouponPage(IPage<Coupon> page, @Param("query") CouponQuery query);

    /**
     * 更新优惠券状态
     * @param couponId 优惠券ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatus(@Param("couponId") Long couponId, @Param("status") Integer status);

    /**
     * 检查优惠券名称唯一性
     * @param name 优惠券名称
     * @param excludeId 排除的ID
     * @return 存在的记录数
     */
    int checkNameUnique(@Param("name") String name, 
                       @Param("excludeId") Long excludeId);

    /**
     * 自动过期优惠券
     * @param currentTime 当前时间
     * @return 过期数量
     */
    int expireCoupons(@Param("currentTime") Date currentTime);
}




