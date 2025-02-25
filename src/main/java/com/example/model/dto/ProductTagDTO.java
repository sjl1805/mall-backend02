package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品标签数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品标签DTO")
public class ProductTagDTO {
    
    @Schema(description = "标签ID")
    private Long id;
    
    @Schema(description = "标签名称")
    private String name;
    
    @Schema(description = "标签类型：1-风格 2-场景 3-功能 4-其他")
    private Integer type;
} 