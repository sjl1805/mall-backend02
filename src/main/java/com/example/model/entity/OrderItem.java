package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "订单商品项实体")
public class OrderItem implements Serializable {
    /**
     * 订单商品ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "订单商品ID", example = "1")
    private Long id;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID", example = "1001")
    private Long orderId;

    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "2001")
    private Long productId;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称", example = "苹果手机 iPhone 14")
    private String productName;

    /**
     * 商品主图URL
     */
    @Schema(description = "商品主图URL", example = "http://example.com/images/iphone14.jpg")
    private String productImage;

    /**
     * 商品单价
     */
    @Schema(description = "商品单价", example = "5999.00")
    private BigDecimal price;

    /**
     * 购买数量
     */
    @Schema(description = "购买数量", example = "2")
    private Integer quantity;

    /**
     * 商品总价
     */
    @Schema(description = "商品总价", example = "11998.00")
    private BigDecimal totalAmount;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-01T12:30:00")
    private LocalDateTime updateTime;

    /**
     * 商品规格信息，非数据库字段
     */
    @TableField(exist = false)
    @Schema(description = "商品规格信息", example = "黑色,256GB")
    private String specifications;

    /**
     * 评价状态：0未评价 1已评价，非数据库字段
     */
    @TableField(exist = false)
    @Schema(description = "评价状态：0未评价 1已评价", example = "0")
    private Integer reviewStatus;

    /**
     * 退款状态：0正常 1申请中 2已退款，非数据库字段
     */
    @TableField(exist = false)
    @Schema(description = "退款状态：0正常 1申请中 2已退款", example = "0")
    private Integer refundStatus;

    /**
     * 是否可评价，非数据库字段
     */
    @TableField(exist = false)
    @Schema(description = "是否可评价", example = "true")
    private Boolean canReview;

    /**
     * 是否可申请退款，非数据库字段
     */
    @TableField(exist = false)
    @Schema(description = "是否可申请退款", example = "true")
    private Boolean canRefund;

    /**
     * 商品类目名称，非数据库字段
     */
    @TableField(exist = false)
    @Schema(description = "商品类目名称", example = "手机数码")
    private String categoryName;

    /**
     * 商品当前价格，非数据库字段
     */
    @TableField(exist = false)
    @Schema(description = "商品当前价格", example = "5899.00")
    private BigDecimal currentPrice;
}