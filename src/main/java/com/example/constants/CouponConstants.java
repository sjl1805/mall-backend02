package com.example.constants;

/**
 * 优惠券常量类
 */
public class CouponConstants {

    /**
     * 优惠券类型：满减
     */
    public static final int TYPE_FULL_REDUCTION = 1;

    /**
     * 优惠券类型：折扣
     */
    public static final int TYPE_DISCOUNT = 2;

    /**
     * 优惠券类型：无门槛
     */
    public static final int TYPE_NO_THRESHOLD = 3;

    /**
     * 优惠券类型描述
     */
    public static final String[] TYPE_DESC = {
            "未知", "满减券", "折扣券", "无门槛券"
    };

    /**
     * 优惠券状态：禁用
     */
    public static final int STATUS_DISABLED = 0;

    /**
     * 优惠券状态：启用
     */
    public static final int STATUS_ENABLED = 1;

    /**
     * 优惠券状态描述
     */
    public static final String[] STATUS_DESC = {
            "禁用", "启用"
    };
    
    /**
     * 默认热门优惠券数量
     */
    public static final int DEFAULT_HOT_COUPON_LIMIT = 10;
    
    /**
     * 默认即将过期天数
     */
    public static final int DEFAULT_EXPIRING_DAYS = 3;
    
    /**
     * 用户优惠券状态：未使用
     */
    public static final int USER_COUPON_STATUS_UNUSED = 0;
    
    /**
     * 用户优惠券状态：已使用
     */
    public static final int USER_COUPON_STATUS_USED = 1;
    
    /**
     * 用户优惠券状态：已过期
     */
    public static final int USER_COUPON_STATUS_EXPIRED = 2;
} 