package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 商品分类数据传输对象
 */
@Data
public class CategoryDTO {

    /**
     * 分类ID
     */
    private Long id;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50个字符")
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
     * 子分类列表
     */
    private List<CategoryDTO> children;
} 