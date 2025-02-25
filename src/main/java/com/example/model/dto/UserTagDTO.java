package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 用户标签数据传输对象
 */
@Data
public class UserTagDTO {

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
     * 标签类型：1-兴趣 2-行为 3-人口特征 4-其他
     */
    @NotNull(message = "标签类型不能为空")
    @Min(value = 1, message = "标签类型值无效")
    @Max(value = 4, message = "标签类型值无效")
    private Integer type;
} 