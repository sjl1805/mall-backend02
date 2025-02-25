package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 推荐结果实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("recommendation_result")
public class RecommendationResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 推荐记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 被推荐用户ID
     */
    private Long userId;
    
    /**
     * 推荐商品ID
     */
    private Long productId;
    
    /**
     * 推荐分数（根据算法模型生成）
     */
    private BigDecimal score;
    
    /**
     * 算法类型：1-基于用户的CF 2-基于物品的CF 3-混合CF 4-热门推荐 5-新品推荐
     */
    private Integer algorithmType;
    
    /**
     * 推荐结果过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 生成时间
     */
    private LocalDateTime createTime;
} 