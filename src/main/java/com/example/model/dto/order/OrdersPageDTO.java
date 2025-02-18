package com.example.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "订单分页查询参数")
public class OrdersPageDTO {
    @Schema(description = "订单号（模糊查询）", example = "202308150001")
    private String orderNo;

    @Schema(description = "用户ID（精确查询）", example = "123")
    private Long userId;

    @Schema(description = "订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消", 
           allowableValues = {"0", "1", "2", "3", "4"}, example = "1")
    private Integer status;

    @Schema(description = "支付方式：1-支付宝 2-微信 3-银联", 
           allowableValues = {"1", "2", "3"}, example = "1")
    private Integer paymentMethod;

    @Schema(description = "物流公司", example = "顺丰速运")
    private String logisticsCompany;

    @Schema(description = "最小金额", example = "100.00")
    private BigDecimal minAmount;

    @Schema(description = "最大金额", example = "500.00")
    private BigDecimal maxAmount;

    @Schema(description = "创建时间起始", example = "2023-01-01 00:00:00")
    private Date createTimeStart;

    @Schema(description = "创建时间结束", example = "2023-12-31 23:59:59")
    private Date createTimeEnd;

    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "排序字段", 
           allowableValues = {"create_time", "payment_time", "pay_amount"}, 
           example = "create_time")
    private String sortField;

    @Schema(description = "排序方式", 
           allowableValues = {"ASC", "DESC"}, 
           example = "DESC")
    private String sortOrder;
} 