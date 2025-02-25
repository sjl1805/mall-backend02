package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类实体类
 */
@Data
@TableName("category")
public class Category {
    
    /**
     * 分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 分类图片
     */
    private String image;
    
    /**
     * 层级：1一级 2二级 3三级
     */
    private Integer level;
    
    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 子分类列表，不映射到数据库字段
     */
    @TableField(exist = false)
    private List<Category> children;
} 