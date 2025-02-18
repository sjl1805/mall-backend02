package com.example.model.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "用户地址分页查询参数")
public class UserAddressPageDTO {
    @Schema(description = "用户ID（精确查询）", example = "123")
    private Long userId;

    @Schema(description = "默认地址状态：0-非默认 1-默认", 
           allowableValues = {"0", "1"}, example = "1")
    private Integer isDefault;

    @Schema(description = "创建时间起始", example = "2023-01-01 00:00:00")
    private Date createTimeStart;

    @Schema(description = "创建时间结束", example = "2023-12-31 23:59:59")
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