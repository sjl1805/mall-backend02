package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 用户行为记录表
 * @TableName user_behavior
 */
@TableName(value ="user_behavior")
@Data
public class UserBehavior {
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
    private Date behaviorTime;

    /**
     * 停留时长（秒）
     */
    private Integer duration;

    /**
     * 创建时间（带时区）
     */
    private Date createTime;

    /**
     * 更新时间（带时区）
     */
    private Date updateTime;

    /**
     * 行为权重
     */
    private BigDecimal weight;
}