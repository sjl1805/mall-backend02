package com.example.model.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "用户行为分页查询参数")
public class UserBehaviorPageDTO {
    @Schema(description = "用户ID（精确查询）")
    private Long userId;

    @Schema(description = "商品ID（精确查询）")
    private Long productId;

    @Schema(description = "行为类型：1-浏览 2-收藏 3-购买")
    private Integer behaviorType;

    @Schema(description = "行为时间起始")
    private Date behaviorTimeStart;

    @Schema(description = "行为时间结束")
    private Date behaviorTimeEnd;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer size = 10;

    @Schema(description = "排序字段", allowableValues = {"behaviorTime", "createTime"})
    private String sortField;

    @Schema(description = "排序方式", allowableValues = {"ASC", "DESC"})
    private String sortOrder;
} 