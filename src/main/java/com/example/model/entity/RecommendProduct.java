package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 推荐商品表
 *
 * @TableName recommend_product
 */
@TableName(value = "recommend_product")
@Data
public class RecommendProduct implements Serializable {
    /**
     * 推荐商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 推荐类型：1-热门商品 2-新品推荐 3-算法生成
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 推荐状态：0-未生效 1-生效中
     */
    private Integer status;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 算法版本
     */
    private String algorithmVersion;

    /**
     * 推荐理由
     */
    private String recommendReason;

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
}