package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.entity.Coupon;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:19
 * @Entity model.entity.Coupon
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 分页查询优惠券
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    Page<Coupon> selectCouponPage(Page<Coupon> page, @Param("query") CouponQuery query);

    /**
     * 批量更新优惠券状态
     * @param ids 优惠券ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatusBatch(@Param("ids") List<Long> ids, 
                        @Param("status") Integer status);

    /**
     * 更新优惠券有效期
     * @param couponId 优惠券ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 影响行数
     */
    int updateValidity(@Param("couponId") Long couponId,
                      @Param("startTime") Date startTime,
                      @Param("endTime") Date endTime);

    /**
     * 查询用户可用优惠券
     * @param orderAmount 订单金额
     * @return 可用优惠券列表
     */
    List<Coupon> selectAvailableCoupons(@Param("orderAmount") BigDecimal orderAmount);

    /**
     * 优惠券使用统计
     * @param couponType 优惠券类型
     * @return 统计结果Map
     */
    Map<String, Object> selectUsageStatistics(@Param("couponType") Integer couponType);

    // 查询条件封装类
    class CouponQuery {
        private Integer status;
        private String name;
        private Date startTime;
        private Date endTime;
    }
}




