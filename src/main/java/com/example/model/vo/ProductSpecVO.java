package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductSpecVO {
    private Long id;

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "规格名称", example = "颜色")
    private String specName;

    @Schema(description = "规格值，JSON格式", example = "{\"color\":\"red\",\"size\":\"M\"}")
    private String specValues; // 保持JSON字符串格式

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 转换JSON字符串为对象
    public void setSpecValues(String specValues) {
        // 实际实现需使用JSON工具类转换
        // 示例：this.specValues = JSONUtil.parseObj(specValues);
    }
} 