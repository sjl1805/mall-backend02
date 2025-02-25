package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
public class FavoriteFolder implements Serializable {
    /**
     * 收藏夹ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收藏夹名称
     */
    private String name;

    /**
     * 收藏夹描述
     */
    private String description;

    /**
     * 公开状态：0-私密 1-公开
     */
    private Integer isPublic;

    /**
     * 收藏项数量
     */
    private Integer itemCount;

    /**
     * 排序
     */
    private Integer sort;

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
     * 非数据库字段，创建者昵称
     */
    @TableField(exist = false)
    private String creatorName;

    /**
     * 非数据库字段，创建者头像
     */
    @TableField(exist = false)
    private String creatorAvatar;

    /**
     * 非数据库字段，收藏夹缩略图（通常是第一个收藏商品的图片）
     */
    @TableField(exist = false)
    private String thumbnail;

    /**
     * 非数据库字段，近期添加的商品ID列表
     */
    @TableField(exist = false)
    private List<Long> recentProductIds;

    /**
     * 非数据库字段，是否是默认收藏夹
     */
    @TableField(exist = false)
    private Boolean isDefault;

    /**
     * 非数据库字段，收藏夹内商品简要信息
     */
    @TableField(exist = false)
    private List<Map<String, Object>> productSnapshots;
}