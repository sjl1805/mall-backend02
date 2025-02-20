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
 * 商品规格表
 *
 * @TableName product_spec
 */
@Schema(description = "商品规格实体")
@TableName(value = "product_spec")
@Data
public class ProductSpec implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 规格ID
     */
    @Schema(description = "规格ID", example = "6001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "2001")
    private Long productId;
    /**
     * 规格名称
     */
    @Schema(description = "规格名称", example = "颜色")
    private String specName;
    /**
     * 规格值，JSON格式
     */
    @Schema(description = "规格选项JSON", example = "[\"黑色\",\"白色\",\"银色\"]")
    private String specValues;
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