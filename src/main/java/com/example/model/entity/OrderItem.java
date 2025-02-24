package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品表
 *
 * @TableName order_item
 */
@TableName(value = "order_item", autoResultMap = true)
@Data
@Schema(description = "订单项实体")
public class OrderItem {
    /**
     * 订单商品ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "订单项ID", example = "1")
    private Long id;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID", example = "10001")
    private Long orderId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", example = "2001")
    private Long productId;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 128, message = "商品名称最长128个字符")
    @Schema(description = "商品名称", example = "华为Mate60 Pro")
    private String productName;

    /**
     * 商品主图URL
     */
    @Schema(description = "商品主图URL", example = "/images/product/2001.jpg")
    private String productImage;

    /**
     * 商品单价
     */
    @NotNull(message = "商品单价不能为空")
    @DecimalMin(value = "0.01", message = "单价不能小于0.01")
    @Schema(description = "商品单价", example = "6999.00")
    private BigDecimal price;

    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量至少为1")
    @Schema(description = "购买数量", example = "2")
    private Integer quantity;

    /**
     * 商品总价
     */
    @NotNull(message = "商品总价不能为空")
    @DecimalMin(value = "0.01", message = "总价不能小于0.01")
    @Schema(description = "商品总价", example = "13998.00")
    private BigDecimal totalAmount;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-03-18 14:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-03-18 14:05:00")
    private LocalDateTime updateTime;
}