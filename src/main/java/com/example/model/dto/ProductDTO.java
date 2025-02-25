package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品数据传输对象
 */
@Data
public class ProductDTO {

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称长度不能超过100个字符")
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 价格
     */
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    private BigDecimal price;

    /**
     * 库存
     */
    @NotNull(message = "商品库存不能为空")
    @Min(value = 0, message = "商品库存不能小于0")
    private Integer stock;

    /**
     * 主图URL
     */
    @NotBlank(message = "商品主图不能为空")
    private String imageMain;

    /**
     * 商品图片列表
     */
    private List<String> images;

    /**
     * 商品标签列表
     */
    private List<Map<String, Object>> tags;

    /**
     * 商品状态：0-下架 1-上架
     */
    private Integer status;

    /**
     * 分类名称（非数据库字段）
     */
    private String categoryName;
} 