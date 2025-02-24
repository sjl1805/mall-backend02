package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;

/**
 * 购物车表
 *
 * @TableName cart
 */
@TableName(value = "cart")
@Data
@Schema(description = "购物车实体")
public class Cart {
    /**
     * 购物车ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "购物车ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", example = "2001")
    private Long productId;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量至少为1")
    @Schema(description = "商品数量", example = "2")
    private Integer quantity;

    /**
     * 选中状态：0-未选中 1-已选中
     */
    @Range(min = 0, max = 1, message = "选中状态参数不合法")
    @Schema(description = "选中状态 0-未选中 1-已选中", example = "1")
    private Integer checked = 1;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 00:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-01-01 00:00:00")
    private LocalDateTime updateTime;
}