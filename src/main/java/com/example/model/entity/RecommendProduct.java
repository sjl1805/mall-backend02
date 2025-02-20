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
 * 推荐商品表
 *
 * @TableName recommend_product
 */
@Schema(description = "推荐商品实体")
@TableName(value = "recommend_product")
@Data
public class RecommendProduct implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 推荐商品ID
     */
    @Schema(description = "推荐记录ID", example = "4001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "2001")
    private Long productId;
    /**
     * 推荐类型：1-热门商品 2-新品推荐
     */
    @Schema(description = "推荐类型：1-热门 2-新品", example = "1")
    private Integer type;
    /**
     * 排序
     */
    @Schema(description = "排序权重", example = "10")
    private Integer sort;
    /**
     * 推荐状态：0-未生效 1-生效中
     */
    @Schema(description = "推荐状态：0-未生效 1-生效", example = "1")
    private Integer status;
    /**
     * 开始时间
     */
    @Schema(description = "推荐开始时间", example = "2025-01-01T00:00:00")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @Schema(description = "推荐结束时间", example = "2025-12-31T23:59:59")
    private LocalDateTime endTime;
    /**
     * 算法版本
     */
    @Schema(description = "推荐算法版本", example = "v2.1.0")
    private String algorithmVersion;
    /**
     * 推荐理由
     */
    @Schema(description = "推荐原因说明", example = "根据用户浏览历史推荐")
    private String recommendReason;
    /**
     * 创建时间（带时区）
     */
    @Schema(description = "创建时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
}