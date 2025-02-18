package com.example.model.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "用户分页查询参数")
public class UserPageDTO {
    @Schema(description = "用户名（模糊查询）")
    private String username;

    @Schema(description = "手机号（精确查询）")
    private String phone;

    @Schema(description = "邮箱（精确查询）")
    private String email;

    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;

    @Schema(description = "角色：0-超级管理员 1-管理员 2-普通用户")
    private Integer role;

    @Schema(description = "注册时间起始")
    private Date createTimeStart;

    @Schema(description = "注册时间结束")
    private Date createTimeEnd;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer size = 10;

    @Schema(description = "排序字段", allowableValues = {"createTime", "updateTime"})
    private String sortField;

    @Schema(description = "排序方式", allowableValues = {"ASC", "DESC"})
    private String sortOrder;
} 