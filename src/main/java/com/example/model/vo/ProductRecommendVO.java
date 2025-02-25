package com.example.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品推荐视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRecommendVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 推荐记录ID
     */
    private Long id;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品图片
     */
    private String productImage;
    
    /**
     * 商品价格
     */
    private BigDecimal productPrice;
    
    /**
     * 商品描述
     */
    private String productDescription;
    
    /**
     * 推荐分数
     */
    private BigDecimal score;
    
    /**
     * 算法类型：1-基于用户的CF 2-基于物品的CF 3-混合CF 4-热门推荐 5-新品推荐
     */
    private Integer algorithmType;
    
    /**
     * 算法类型名称
     */
    private String algorithmTypeName;
    
    /**
     * 推荐理由
     */
    private String recommendReason;
    
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 生成时间
     */
    private LocalDateTime createTime;
} 