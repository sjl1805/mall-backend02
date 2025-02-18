package com.example.model.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "用户优惠券分页查询参数")
public class UserCouponPageDTO {
    @Schema(description = "用户ID（精确查询）", example = "123")
    private Long userId;

    @Schema(description = "优惠券状态：0-未使用 1-已使用 2-已过期", 
           allowableValues = {"0", "1", "2"}, example = "0")
    private Integer status;

    @Schema(description = "领取时间起始", example = "2023-01-01")
    private Date getTimeStart;

    @Schema(description = "领取时间结束", example = "2023-12-31")
    private Date getTimeEnd;

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "排序字段", 
           allowableValues = {"get_time", "use_time"}, 
           example = "get_time")
    private String sortField;

    @Schema(description = "排序方式", 
           allowableValues = {"ASC", "DESC"}, 
           example = "DESC")
    private String sortOrder;
} 