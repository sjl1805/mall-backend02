package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品信息DTO")
public class ProductDTO {
    
    @Schema(description = "商品ID")
    private Long id;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "分类名称")
    private String categoryName;
    
    @Schema(description = "商品名称")
    private String name;
    
    @Schema(description = "商品描述")
    private String description;
    
    @Schema(description = "价格")
    private BigDecimal price;
    
    @Schema(description = "库存")
    private Integer stock;
    
    @Schema(description = "主图URL")
    private String imageMain;
    
    @Schema(description = "商品图片列表")
    private List<String> imageList;
    
    @Schema(description = "商品标签列表")
    private List<ProductTagDTO> tagList;
    
    @Schema(description = "商品状态：0-下架 1-上架")
    private Integer status;
    
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
} 