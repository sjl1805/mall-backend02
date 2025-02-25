package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 收藏夹表
 *
 * @TableName favorite_folder
 */
@TableName(value = "favorite_folder")
@Data
@Schema(description = "收藏夹实体")
public class FavoriteFolder implements Serializable {
    /**
     * 收藏夹ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "收藏夹ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    /**
     * 收藏夹名称
     */
    @Schema(description = "收藏夹名称", example = "我喜欢的商品")
    private String name;

    /**
     * 收藏夹描述
     */
    @Schema(description = "收藏夹描述", example = "这里收集了我最喜欢的电子产品")
    private String description;

    /**
     * 公开状态：0-私密 1-公开
     */
    @Schema(description = "公开状态：0-私密 1-公开", example = "1")
    private Integer isPublic;

    /**
     * 收藏项数量
     */
    @Schema(description = "收藏项数量", example = "15")
    private Integer itemCount;

    /**
     * 排序
     */
    @Schema(description = "排序权重", example = "100")
    private Integer sort;

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

    /**
     * 非数据库字段，创建者昵称
     */
    @TableField(exist = false)
    @Schema(description = "创建者昵称", example = "张三")
    private String creatorName;

    /**
     * 非数据库字段，创建者头像
     */
    @TableField(exist = false)
    @Schema(description = "创建者头像", example = "http://example.com/avatars/user1.jpg")
    private String creatorAvatar;

    /**
     * 非数据库字段，收藏夹缩略图（通常是第一个收藏商品的图片）
     */
    @TableField(exist = false)
    @Schema(description = "收藏夹缩略图", example = "http://example.com/images/product1.jpg")
    private String thumbnail;

    /**
     * 非数据库字段，近期添加的商品ID列表
     */
    @TableField(exist = false)
    @Schema(description = "近期添加的商品ID列表")
    private List<Long> recentProductIds;

    /**
     * 非数据库字段，是否是默认收藏夹
     */
    @TableField(exist = false)
    @Schema(description = "是否是默认收藏夹", example = "true")
    private Boolean isDefault;

    /**
     * 非数据库字段，收藏夹内商品简要信息
     */
    @TableField(exist = false)
    @Schema(description = "收藏夹内商品简要信息")
    private List<Map<String, Object>> productSnapshots;
}