package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品评价表
 *
 * @TableName product_review
 */
@Schema(description = "商品评价实体")
@TableName(value = "product_review")
@Data
public class ProductReview implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 评价ID
     */
    @Schema(description = "评价ID", example = "9001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单ID
     */
    @Schema(description = "关联订单ID", example = "6001")
    private Long orderId;
    /**
     * 用户ID
     */
    @Schema(description = "评价用户ID", example = "10001")
    private Long userId;
    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "2001")
    private Long productId;
    /**
     * 评分：1-5分
     */
    @Schema(description = "评分（1-5分）", example = "5")
    private Integer rating;
    /**
     * 评价内容
     */
    @Schema(description = "评价内容", example = "商品质量非常好，物流速度快")
    private String content;
    /**
     * 评价图片，JSON格式
     */
    @Schema(description = "评价图片数组", example = "[\"/images/reviews/1.jpg\"]")
    private String images;
    /**
     * 审核状态：0-待审核 1-已通过 2-已拒绝
     */
    @Schema(description = "审核状态：0-待审 1-通过 2-拒绝", example = "1")
    private Integer status;
    /**
     * 创建时间（带时区）
     */
    @Schema(description = "评价时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "最后更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
}