package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * 用户行为记录表
 *
 * @TableName user_behavior
 */
@TableName(value = "user_behavior")
@Data
public class UserBehavior implements Serializable {
    /**
     * 行为记录ID
     */
    @TableId(type = IdType.AUTO)
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
     * 行为类型：1-浏览 2-收藏 3-购买
     */
    private Integer behaviorType;

    /**
     * 行为时间
     */
    private LocalDateTime behaviorTime;

    /**
     * 停留时长（秒）
     */
    private Integer duration;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 行为权重
     */
    private BigDecimal weight;
}