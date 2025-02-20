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
import java.util.List;

/**
 * 商品分类表
 *
 * @TableName category
 */
@Schema(description = "商品分类实体")
@TableName(value = "category")
@Data
public class Category implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "1001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 父分类ID
     */
    @Schema(description = "父分类ID（0表示根分类）", example = "0")
    private Long parentId;
    /**
     * 分类名称
     */
    @Schema(description = "分类名称", example = "手机数码")
    private String name;
    /**
     * 分类图标
     */
    @Schema(description = "分类图标URL", example = "/images/category/phone.png")
    private String icon;
    /**
     * 层级：1一级 2二级 3三级
     */
    @Schema(description = "分类层级：1-一级 2-二级 3-三级", example = "1")
    private Integer level;
    /**
     * 排序
     */
    @Schema(description = "排序值（越小越靠前）", example = "10")
    private Integer sort;
    /**
     * 状态：0-禁用 1-启用
     */
    @Schema(description = "状态：0-禁用 1-启用", example = "1")
    private Integer status;
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
    /**
     * 子分类集合
     */
    @Schema(description = "子分类列表")
    @TableField(exist = false)
    private List<Category> children;
}