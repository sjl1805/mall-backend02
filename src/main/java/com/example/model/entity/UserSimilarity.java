package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户相似度实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_similarity")
public class UserSimilarity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户A ID
     */
    private Long userIdA;
    
    /**
     * 用户B ID
     */
    private Long userIdB;
    
    /**
     * 相似度分数（0.0-1.0，余弦相似度计算）
     */
    private BigDecimal similarity;
    
    /**
     * 最后计算时间
     */
    private LocalDateTime updateTime;
} 