package com.example.model.dto;

import lombok.Data;

/**
 * 用户标签权重数据传输对象
 */
@Data
public class UserTagWeightDTO {

    /**
     * 标签ID
     */
    private Long tagId;
    
    /**
     * 标签名称
     */
    private String tagName;
    
    /**
     * 标签权重：1-100，数值越大表示用户与该标签的关联度越高
     */
    private Integer weight;
} 