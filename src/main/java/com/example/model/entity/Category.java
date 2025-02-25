package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
public class Category implements Serializable {
    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 层级：1一级 2二级 3三级
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /**
     * 非数据库字段，用于存储子分类列表
     */
    @TableField(exist = false)
    private List<Category> children;
    
    /**
     * 非数据库字段，用于存储分类下的商品数量
     */
    @TableField(exist = false)
    private Integer productCount;
}