package com.example.model.vo;

import lombok.Data;

@Data
public class CategoryVO {
    private Long id;
    private Long parentId;
    private String name;
    private String icon;
    private Integer level;
    private Integer sort;
    private String status; // 状态描述
} 