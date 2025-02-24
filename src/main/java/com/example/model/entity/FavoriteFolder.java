package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;

/**
 * 收藏夹表
 *
 * @TableName favorite_folder
 */
@TableName(value = "favorite_folder")
@Data
@Schema(description = "收藏夹实体")
public class FavoriteFolder {
    /**
     * 收藏夹ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "收藏夹ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    /**
     * 收藏夹名称
     */
    @NotBlank(message = "收藏夹名称不能为空")
    @Size(max = 32, message = "名称最长32个字符")
    @Schema(description = "收藏夹名称", example = "电子产品收藏")
    private String name;

    /**
     * 收藏夹描述
     */
    @Size(max = 128, message = "描述最长128个字符")
    @Schema(description = "收藏夹描述", example = "个人电子设备收藏")
    private String description;

    /**
     * 公开状态：0-私密 1-公开
     */
    @Range(min = 0, max = 1, message = "公开状态参数不合法")
    @Schema(description = "公开状态 0-私密 1-公开", example = "0")
    private Integer isPublic = 0;

    /**
     * 收藏项数量
     */
    @Min(value = 0, message = "收藏数量不能小于0")
    @Schema(description = "收藏项数量", example = "0")
    private Integer itemCount = 0;

    /**
     * 排序
     */
    @Min(value = 0, message = "排序值不能小于0")
    @Schema(description = "排序值", example = "0")
    private Integer sort = 0;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 00:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-01-01 00:00:00")
    private LocalDateTime updateTime;
}