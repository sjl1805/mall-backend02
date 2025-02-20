package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户行为记录表
 *
 * @TableName user_behavior
 */
@Schema(description = "用户行为实体")
@TableName(value = "user_behavior")
@Data
public class UserBehavior implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 行为记录ID
     */
    @Schema(description = "行为记录ID", example = "200001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "10001")
    private Long userId;
    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "2001")
    private Long productId;
    /**
     * 行为类型：1-浏览 2-收藏 3-购买
     */
    @Schema(description = "行为类型：1-浏览 2-收藏 3-购买", example = "1")
    private Integer behaviorType;
    /**
     * 行为时间
     */
    @Schema(description = "行为时间", example = "2023-08-01T10:15:30")
    private LocalDateTime behaviorTime;
    /**
     * 停留时长（秒）
     */
    @Schema(description = "停留时长（秒）", example = "120")
    private Integer duration;
    /**
     * 创建时间（带时区）
     */
    @Schema(description = "记录创建时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "最后更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
    /**
     * 行为权重
     */
    @Schema(description = "行为权重系数", example = "0.8")
    private BigDecimal weight;
}