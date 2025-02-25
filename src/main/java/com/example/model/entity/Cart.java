package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车表
 * 该实体类表示用户的购物车记录，包含选中状态和数量信息
 * 购物车是电商系统中连接浏览和下单的关键环节，直接影响用户的购买决策和转化率
 *
 * @TableName cart
 */
@TableName(value = "cart")
@Data
@Schema(description = "购物车实体")
public class Cart implements Serializable {
    /**
     * 非数据库字段，序列化ID
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 购物车ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "购物车ID", example = "1")
    private Long id;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;
    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "2001")
    private Long productId;
    /**
     * 商品数量
     */
    @Schema(description = "商品数量", example = "2")
    private Integer quantity;
    /**
     * 选中状态：0-未选中 1-已选中
     */
    @Schema(description = "选中状态：0-未选中 1-已选中", example = "1")
    private Integer checked;
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
     * 非数据库字段，商品名称
     */
    @TableField(exist = false)
    @Schema(description = "商品名称", example = "苹果手机")
    private String productName;
    /**
     * 非数据库字段，商品价格
     */
    @TableField(exist = false)
    @Schema(description = "商品价格", example = "5999.00")
    private BigDecimal productPrice;
    /**
     * 非数据库字段，商品库存
     */
    @TableField(exist = false)
    @Schema(description = "商品库存", example = "100")
    private Integer productStock;
    /**
     * 非数据库字段，商品图片
     */
    @TableField(exist = false)
    @Schema(description = "商品图片", example = "http://example.com/images/product1.jpg")
    private String productImages;
    /**
     * 非数据库字段，商品总价
     */
    @TableField(exist = false)
    @Schema(description = "商品总价", example = "11998.00")
    private BigDecimal totalPrice;
    /**
     * 非数据库字段，商品状态：0-下架 1-正常
     */
    @TableField(exist = false)
    @Schema(description = "商品状态：0-下架 1-正常", example = "1")
    private Integer productStatus;
    /**
     * 非数据库字段，商品规格信息
     */
    @TableField(exist = false)
    @Schema(description = "商品规格信息", example = "黑色,128GB")
    private String specifications;
    /**
     * 非数据库字段，是否可购买（商品上架且库存足够）
     */
    @TableField(exist = false)
    @Schema(description = "是否可购买（商品上架且库存足够）", example = "true")
    private Boolean canBuy;
}