package com.example.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Schema(description = "用户查询条件")
public class UserQuery {
    @Schema(description = "关键词（用户名/昵称/邮箱）")
    private String keyword;
    
    @Schema(description = "用户状态")
    private Integer status;
    
    @Schema(description = "用户角色")
    private Integer role;
    
    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    
    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方式（ASC/DESC）")
    private String sortOrder;
} 