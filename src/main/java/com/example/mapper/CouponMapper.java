package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.model.entity.Coupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31815
 * @description 针对表【coupon(优惠券表)】的数据库操作Mapper
 * @createDate 2025-02-24 12:04:19
 * @Entity model.entity.Coupon
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 根据优惠券名称模糊查询
     *
     * @param name 优惠券名称
     * @return 优惠券列表
     */
    List<Coupon> selectByNameLike(@Param("name") String name);

    /**
     * 根据优惠券名称查询优惠券
     *
     * @param name 优惠券名称
     * @return 优惠券列表
     */
    List<Coupon> selectByName(@Param("name") String name);

    /**
     * 分页查询优惠券
     *
     * @param page 分页信息
     * @return 优惠券列表
     */
    IPage<Coupon> selectPage(IPage<Coupon> page);

    /**
     * 根据ID查询优惠券
     *
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    Coupon selectById(@Param("id") Long id);

    /**
     * 插入新优惠券
     *
     * @param coupon 优惠券信息
     * @return 插入结果
     */
    int insertCoupon(Coupon coupon);

    /**
     * 更新优惠券信息
     *
     * @param coupon 优惠券信息
     * @return 更新结果
     */
    int updateCoupon(Coupon coupon);

    /**
     * 根据ID删除优惠券
     *
     * @param id 优惠券ID
     * @return 删除结果
     */
    int deleteCoupon(@Param("id") Long id);
}




