package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import java.time.LocalDateTime;

/**
 * 商品分类表
 *
 * @TableName category
 */
@TableName(value = "category")
@Data
@Schema(description = "商品分类实体")
public class Category {
    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "分类ID", example = "1")
    private Long id;

    /**
     * 父分类ID
     */
    @Schema(description = "父分类ID", example = "0")
    private Long parentId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 64, message = "分类名称最长64个字符")
    @Schema(description = "分类名称", example = "电子产品")
    private String name;

    /**
     * 分类图标
     */
    @Schema(description = "分类图标URL", example = "/images/category/electronics.png")
    private String icon;

    /**
     * 层级：1一级 2二级 3三级
     */
    @NotNull(message = "层级不能为空")
    @Range(min = 1, max = 3, message = "层级参数不合法")
    @Schema(description = "分类层级 1-一级 2-二级 3-三级", example = "1")
    private Integer level;

    /**
     * 排序
     */
    @Min(value = 0, message = "排序值不能小于0")
    @Schema(description = "排序值", example = "0")
    private Integer sort = 0;

    /**
     * 状态：0-禁用 1-启用
     */
    @Range(min = 0, max = 1, message = "状态参数不合法")
    @Schema(description = "分类状态 0-禁用 1-启用", example = "1")
    private Integer status = 1;

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