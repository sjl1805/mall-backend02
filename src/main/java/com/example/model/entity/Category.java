package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类表
 * 该实体类代表商城的商品分类体系，支持多级分类结构（最多三级）
 * 每个分类可以有父分类（顶级分类的父ID为0）和多个子分类
 * 包含分类的基本信息如名称、图标、排序等属性
 *
 * @TableName category
 */
@TableName(value = "category")
@Data
@Schema(description = "商品分类实体")
public class Category implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "分类ID", example = "1")
    private Long id;
    /**
     * 父分类ID
     */
    @Schema(description = "父分类ID，顶级分类为0", example = "0")
    private Long parentId;
    /**
     * 分类名称
     */
    @Schema(description = "分类名称", example = "电子产品")
    private String name;
    /**
     * 分类图标
     */
    @Schema(description = "分类图标URL", example = "http://example.com/icons/electronics.png")
    private String icon;
    /**
     * 层级：1一级 2二级 3三级
     */
    @Schema(description = "分类层级：1一级 2二级 3三级", example = "1")
    private Integer level;
    /**
     * 排序
     */
    @Schema(description = "排序优先级", example = "100")
    private Integer sort;
    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", example = "1")
    private Integer status;
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
     * 非数据库字段，用于存储子分类列表
     */
    @TableField(exist = false)
    @Schema(description = "子分类列表")
    private List<Category> children;

    /**
     * 非数据库字段，用于存储分类下的商品数量
     */
    @TableField(exist = false)
    @Schema(description = "分类下的商品数量", example = "42")
    private Integer productCount;
}