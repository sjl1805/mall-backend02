package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品规格表
 *
 * @TableName product_spec
 */
@TableName(value = "product_spec")
@Data
@Schema(description = "商品规格实体")
public class ProductSpec implements Serializable {
    /**
     * 规格ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "规格ID", example = "1")
    private Long id;

    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "100")
    private Long productId;

    /**
     * 规格名称
     */
    @Schema(description = "规格名称", example = "颜色")
    private String specName;

    /**
     * 规格值，JSON格式
     */
    @Schema(description = "规格值，JSON格式", example = "[\"黑色\",\"白色\",\"蓝色\"]")
    private String specValues;

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
     * 非数据库字段，规格值列表（反序列化后）
     */
    @TableField(exist = false)
    @Schema(description = "规格值列表（反序列化后）", example = "[\"黑色\",\"白色\",\"蓝色\"]")
    private List<String> valueList;

    /**
     * 非数据库字段，关联的SKU数量
     */
    @TableField(exist = false)
    @Schema(description = "关联的SKU数量", example = "6")
    private Integer skuCount;

    /**
     * 非数据库字段，是否允许删除
     */
    @TableField(exist = false)
    @Schema(description = "是否允许删除", example = "true")
    private Boolean allowDelete;
}