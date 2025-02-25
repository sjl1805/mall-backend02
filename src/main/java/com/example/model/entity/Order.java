package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName("orders")
public class Order {
    
    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消
     */
    private Integer status;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人电话
     */
    private String receiverPhone;
    
    /**
     * 收货地址
     */
    private String receiverAddress;
    
    /**
     * 使用的优惠券ID
     */
    private Long couponId;
    
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 支付方式：0-未支付 1-微信 2-支付宝
     */
    private Integer paymentType;
    
    /**
     * 收货人省份
     */
    private String receiverProvince;
    
    /**
     * 收货人城市
     */
    private String receiverCity;
    
    /**
     * 收货人区县
     */
    private String receiverDistrict;
    
    /**
     * 订单备注
     */
    private String note;
    
    /**
     * 实付金额
     */
    private BigDecimal payAmount;
    
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    
    /**
     * 订单完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTime;
    
    /**
     * 是否删除：0-未删除 1-已删除
     */
    private Integer deleted;
} 