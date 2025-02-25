package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品分类DTO")
public class CategoryDTO {
    
    @Schema(description = "分类ID")
    private Long id;
    
    @Schema(description = "父分类ID，根分类为0")
    private Long parentId;
    
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "分类图片URL")
    private String image;
    
    @Schema(description = "层级：1-一级 2-二级 3-三级")
    private Integer level;
    
    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "子分类列表，用于构建树形结构")
    private List<CategoryDTO> children;
} 