package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 商品标签数据传输对象
 */
@Data
public class ProductTagDTO {

    /**
     * 标签ID
     */
    private Long id;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    private String name;

    /**
     * 标签类型：1-风格 2-场景 3-功能 4-其他
     */
    @NotNull(message = "标签类型不能为空")
    @Min(value = 1, message = "标签类型值无效")
    @Max(value = 4, message = "标签类型值无效")
    private Integer type;
} 