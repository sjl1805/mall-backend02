package com.example.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductSpecVO {
    private Long id;
    private Long productId;
    private String specName;
    private Object specValues; // 反序列化后的JSON对象
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 转换JSON字符串为对象
    public void setSpecValues(String specValues) {
        // 实际实现需使用JSON工具类转换
        // 示例：this.specValues = JSONUtil.parseObj(specValues);
    }
} 