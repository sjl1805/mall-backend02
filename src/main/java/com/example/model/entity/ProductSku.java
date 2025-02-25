package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 商品SKU实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "product_sku", autoResultMap = true)
public class ProductSku implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * SKU ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * SKU编码
     */
    private String skuCode;
    
    /**
     * 规格JSON，如：{"颜色":"红色","尺寸":"XL"}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> specifications;
    
    /**
     * SKU价格
     */
    private BigDecimal price;
    
    /**
     * SKU库存
     */
    private Integer stock;
    
    /**
     * SKU图片
     */
    private String image;
    
    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 