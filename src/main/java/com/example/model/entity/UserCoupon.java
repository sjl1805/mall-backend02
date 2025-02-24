package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户优惠券表
 * @TableName user_coupon
 */
@TableName(value ="user_coupon")
@Data
public class UserCoupon {
    /**
     * 用户优惠券ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 用户优惠券状态：0-未使用 1-已使用 2-已过期
     */
    private Integer status;

    /**
     * 使用的订单ID
     */
    private Long orderId;

    /**
     * 领取时间
     */
    private Date getTime;

    /**
     * 使用时间
     */
    private Date useTime;

    /**
     * 创建时间（带时区）
     */
    private Date createTime;

    /**
     * 更新时间（带时区）
     */
    private Date updateTime;
}