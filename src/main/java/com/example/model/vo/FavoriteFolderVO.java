package com.example.model.vo;

import lombok.Data;

@Data
public class FavoriteFolderVO {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Integer isPublic;
    private Integer itemCount; // 收藏项数量
    private Integer sort; // 排序字段
} 