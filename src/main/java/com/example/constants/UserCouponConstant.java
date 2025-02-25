package com.example.constants;

/**
 * 用户优惠券常量
 */
public class UserCouponConstant {
    
    /**
     * 优惠券状态：未使用
     */
    public static final int STATUS_UNUSED = 0;
    
    /**
     * 优惠券状态：已使用
     */
    public static final int STATUS_USED = 1;
    
    /**
     * 优惠券状态：已过期
     */
    public static final int STATUS_EXPIRED = 2;
    
    /**
     * 默认优惠券过期提醒天数
     */
    public static final int DEFAULT_EXPIRE_REMIND_DAYS = 3;
    
    /**
     * 默认每页显示数量
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
} 