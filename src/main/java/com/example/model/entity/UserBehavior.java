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
 * 用户行为实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_behavior")
public class UserBehavior implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 行为ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 行为类型：1-浏览 2-点击 3-加入购物车 4-收藏 5-搜索 6-评分 7-评价
     */
    private Integer behaviorType;
    
    /**
     * 搜索关键词
     */
    private String searchKeyword;
    
    /**
     * 停留时间(秒)
     */
    private Integer stayTime;
    
    /**
     * 评分：1-5分
     */
    private BigDecimal rating;
    
    /**
     * 评价内容
     */
    private String reviewContent;
    
    /**
     * 浏览次数
     */
    private Integer viewCount;
    
    /**
     * 加购次数
     */
    private Integer cartCount;
    
    /**
     * 购买次数
     */
    private Integer buyCount;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 用户设备信息
     */
    private String userAgent;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 