package com.example.model.dto;

import lombok.Data;

/**
 * 分类查询数据传输对象
 */
@Data
public class CategoryQueryDTO {

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 层级：1一级 2二级 3三级
     */
    private Integer level;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式：asc/desc
     */
    private String sortOrder;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    private Integer pageSize = 10;
} 