package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车表
 *
 * @TableName cart
 */
@Schema(description = "购物车实体")
@TableName(value = "cart")
@Data
public class Cart implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 购物车ID
     */
    @Schema(description = "购物车ID", example = "1")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "10001")
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
    @Schema(description = "创建时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
}