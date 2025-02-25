package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品表
 *
 * @TableName order_item
 */
@TableName(value = "order_item")
@Data
public class OrderItem implements Serializable {
    /**
     * 订单商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图URL
     */
    private String productImage;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 商品总价
     */
    private BigDecimal totalAmount;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 商品规格信息，非数据库字段
     */
    @TableField(exist = false)
    private String specifications;

    /**
     * 评价状态：0未评价 1已评价，非数据库字段
     */
    @TableField(exist = false)
    private Integer reviewStatus;

    /**
     * 退款状态：0正常 1申请中 2已退款，非数据库字段
     */
    @TableField(exist = false)
    private Integer refundStatus;

    /**
     * 是否可评价，非数据库字段
     */
    @TableField(exist = false)
    private Boolean canReview;

    /**
     * 是否可申请退款，非数据库字段
     */
    @TableField(exist = false)
    private Boolean canRefund;

    /**
     * 商品类目名称，非数据库字段
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 商品当前价格，非数据库字段
     */
    @TableField(exist = false)
    private BigDecimal currentPrice;
}