package com.example.model.dto.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "优惠券分页查询参数")
public class CouponPageDTO {
    @Schema(description = "优惠券名称（模糊查询）")
    private String name;

    @Schema(description = "类型：1-满减券 2-折扣券", allowableValues = {"1", "2"})
    private Integer type;

    @Schema(description = "状态：0-失效 1-生效", allowableValues = {"0", "1"})
    private Integer status;

    @Schema(description = "生效时间起始")
    private Date startTimeStart;

    @Schema(description = "生效时间结束")
    private Date startTimeEnd;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer size = 10;

    @Schema(description = "排序字段", allowableValues = {"start_time", "end_time", "create_time"})
    private String sortField;

    @Schema(description = "排序方式", allowableValues = {"ASC", "DESC"})
    private String sortOrder;
} 