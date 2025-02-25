package com.example.model.dto;

import lombok.Data;

/**
 * 标签查询数据传输对象
 */
@Data
public class TagQueryDTO {

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 标签类型
     */
    private Integer type;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

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