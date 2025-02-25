package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 订单项实体类
 */
@Data
@TableName(value = "order_item", autoResultMap = true)
public class OrderItem {
    
    /**
     * 订单商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * SKU ID
     */
    private Long skuId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品主图URL
     */
    private String productImage;
    
    /**
     * 商品规格JSON
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> specifications;
    
    /**
     * 商品单价
     */
    private BigDecimal price;
    
    /**
     * 购买数量
     */
    private Integer quantity;
    
    /**
     * 商品总价
     */
    private BigDecimal totalAmount;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * SKU属性JSON字符串
     */
    private String skuProperties;
} 