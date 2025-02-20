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
 * 收藏夹表
 *
 * @TableName favorite_folder
 */
@Schema(description = "收藏夹实体")
@TableName(value = "favorite_folder")
@Data
public class FavoriteFolder implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 收藏夹ID
     */
    @Schema(description = "收藏夹ID", example = "3001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @Schema(description = "所属用户ID", example = "10001")
    private Long userId;
    /**
     * 收藏夹名称
     */
    @Schema(description = "收藏夹名称", example = "我的数码收藏")
    private String name;
    /**
     * 收藏夹描述
     */
    @Schema(description = "收藏夹描述", example = "数码产品收藏列表")
    private String description;
    /**
     * 公开状态：0-私密 1-公开
     */
    @Schema(description = "公开状态：0-私密 1-公开", example = "1")
    private Integer isPublic;
    /**
     * 收藏项数量
     */
    @Schema(description = "收藏商品数量", example = "15")
    private Integer itemCount;
    /**
     * 排序
     */
    @Schema(description = "排序值（越小越靠前）", example = "5")
    private Integer sort;
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