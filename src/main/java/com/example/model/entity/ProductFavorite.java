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
 * 商品收藏表
 *
 * @TableName product_favorite
 */
@Schema(description = "商品收藏实体")
@TableName(value = "product_favorite")
@Data
public class ProductFavorite implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 收藏ID
     */
    @Schema(description = "收藏记录ID", example = "8001")
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
     * 收藏夹ID（NULL表示未分类）
     */
    @Schema(description = "所属收藏夹ID（空表示未分类）", example = "null")
    private Long folderId;
    /**
     * 创建时间（带时区）
     */
    @Schema(description = "收藏时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "最后更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
}