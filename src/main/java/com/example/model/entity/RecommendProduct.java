package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推荐商品表
 *
 * @TableName recommend_product
 */
@TableName(value = "recommend_product")
@Data
@Schema(description = "推荐商品实体")
public class RecommendProduct implements Serializable {
    /**
     * 推荐商品ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "推荐商品ID", example = "1")
    private Long id;

    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "100")
    private Long productId;

    /**
     * 推荐类型：1-热门商品 2-新品推荐 3-算法生成
     */
    @Schema(description = "推荐类型：1-热门商品 2-新品推荐 3-算法生成", example = "1")
    private Integer type;

    /**
     * 排序
     */
    @Schema(description = "排序权重", example = "100")
    private Integer sort;

    /**
     * 推荐状态：0-未生效 1-生效中
     */
    @Schema(description = "推荐状态：0-未生效 1-生效中", example = "1")
    private Integer status;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间", example = "2023-01-01T00:00:00")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间", example = "2023-12-31T23:59:59")
    private LocalDateTime endTime;

    /**
     * 算法版本
     */
    @Schema(description = "算法版本", example = "v2.3.1")
    private String algorithmVersion;

    /**
     * 推荐理由
     */
    @Schema(description = "推荐理由", example = "根据您的浏览历史推荐")
    private String recommendReason;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-01T12:30:00")
    private LocalDateTime updateTime;
}