package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.util.Date;

/**
 * 用户查询条件封装类
 */
@Data
@Schema(description = "用户查询条件")
public class UserQuery {
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;
    
    @Schema(description = "角色：0-超级管理员 1-管理员 2-普通用户")
    private Integer role;
    
    @Schema(description = "注册时间起始")
    private Date createTimeStart;
    
    @Schema(description = "注册时间结束")
    private Date createTimeEnd;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方式：ASC/DESC")
    private String sortOrder;

    @Schema(description = "页码")
    private Integer page = 1;

    @Schema(description = "每页数量")
    private Integer size = 10;
} 