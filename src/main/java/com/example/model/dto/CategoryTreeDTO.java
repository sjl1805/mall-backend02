package com.example.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 分类树数据传输对象
 */
@Data
public class CategoryTreeDTO {

    /**
     * 分类ID
     */
    private Long id;

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
     * 子分类
     */
    private List<CategoryTreeDTO> children;

    /**
     * 该分类下的商品数量
     */
    private Integer productCount;
} 