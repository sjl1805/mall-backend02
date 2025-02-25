package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户标签关联数据传输对象
 */
@Data
public class UserTagRelationDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 标签ID列表
     */
    private List<Long> tagIds;
    
    /**
     * 标签权重映射，key为标签ID，value为权重
     */
    private List<UserTagWeightDTO> tagWeights;
} 