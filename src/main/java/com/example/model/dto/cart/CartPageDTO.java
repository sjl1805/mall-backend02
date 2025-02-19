package com.example.model.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "购物车分页查询参数")
public class CartPageDTO {
    @Schema(description = "用户ID（精确查询）", example = "123")
    private Long userId;

    @Schema(description = "商品ID（精确查询）", example = "456")
    private Long productId;

    @Schema(description = "选中状态：0-未选中 1-已选中",
            allowableValues = {"0", "1"}, example = "1")
    private Integer checked;

    @Schema(description = "创建时间起始", example = "2023-01-01")
    private Date createTimeStart;

    @Schema(description = "创建时间结束", example = "2023-12-31")
    private Date createTimeEnd;

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "排序字段",
            allowableValues = {"createTime", "updateTime"},
            example = "createTime")
    private String sortField;

    @Schema(description = "排序方式",
            allowableValues = {"ASC", "DESC"},
            example = "DESC")
    private String sortOrder;
} 