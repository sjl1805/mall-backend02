package com.example.model.dto;

import com.example.model.entity.UserBehavior;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Schema(description = "用户行为记录DTO")
public class UserBehaviorDTO {
    @Schema(description = "行为记录ID（更新时必填）", example = "789")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "456")
    private Long productId;

    @NotNull(message = "行为类型不能为空")
    @Min(1)
    @Max(3)
    @Schema(description = "行为类型：1-浏览 2-收藏 3-购买", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer behaviorType;

    @Schema(description = "行为时间", example = "2023-08-01 12:30:45")
    private LocalDateTime behaviorTime;

    @Min(value = 0, message = "停留时长不能为负数")
    @Schema(description = "停留时长（秒）", example = "60")
    private Integer duration = 0;

    @DecimalMin("0.1")
    @DecimalMax("2.0")
    @Schema(description = "行为权重", example = "1.2")
    private Integer weight = 0;

    public static UserBehaviorDTO fromEntity(UserBehavior behavior) {
        UserBehaviorDTO dto = new UserBehaviorDTO();
        dto.setId(behavior.getId());
        dto.setUserId(behavior.getUserId());
        dto.setProductId(behavior.getProductId());
        dto.setBehaviorType(behavior.getBehaviorType());
        dto.setBehaviorTime(behavior.getBehaviorTime());
        dto.setDuration(behavior.getDuration());
        dto.setWeight(behavior.getWeight());
        return dto;
    }

    public UserBehavior toEntity() {
        UserBehavior behavior = new UserBehavior();
        behavior.setId(this.id);
        behavior.setUserId(this.userId);
        behavior.setProductId(this.productId);
        behavior.setBehaviorType(this.behaviorType);
        behavior.setBehaviorTime(this.behaviorTime);
        behavior.setDuration(this.duration);
        behavior.setWeight(this.weight);
        return behavior;
    }
} 