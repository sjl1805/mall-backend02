package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 推荐结果数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "推荐结果DTO")
public class RecommendationDTO {
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "推荐商品列表")
    private List<ProductDTO> recommendedProducts;
    
    @Schema(description = "算法类型：1-基于用户的CF 2-基于物品的CF 3-混合CF 4-热门推荐 5-新品推荐")
    private Integer algorithmType;
    
    @Schema(description = "算法类型描述")
    private String algorithmDesc;
    
    @Schema(description = "平均推荐分数")
    private BigDecimal averageScore;
    
    @Schema(description = "总推荐数量")
    private Integer totalRecommendations;
} 