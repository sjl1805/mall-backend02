package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户相似度实体类
 */
@Data
@TableName("user_similarity")
public class UserSimilarity {
    
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 