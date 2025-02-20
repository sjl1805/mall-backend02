package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品表
 *
 * @TableName order_item
 */
@Schema(description = "订单商品项实体")
@TableName(value = "order_item")
@Data
public class OrderItem implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 订单商品ID
     */
    @Schema(description = "订单项ID", example = "7001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单ID
     */
    @Schema(description = "关联订单ID", example = "6001")
    private Long orderId;
    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "2001")
    private Long productId;
    /**
     * 商品名称
     */
    @Schema(description = "商品名称", example = "华为Mate50")
    private String productName;
    /**
     * 商品主图URL
     */
    @Schema(description = "商品主图地址", example = "/images/products/mate50.jpg")
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
    @Schema(description = "商品总金额", example = "11998.00")
    private BigDecimal totalAmount;
    /**
     * 创建时间（带时区）
     */
    @Schema(description = "创建时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
}