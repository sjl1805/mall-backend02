package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车表
 * 该实体类表示用户的购物车记录，包含选中状态和数量信息
 * 购物车是电商系统中连接浏览和下单的关键环节，直接影响用户的购买决策和转化率
 *
 * @TableName cart
 */
@TableName(value = "cart")
@Data
public class Cart implements Serializable {
    /**
     * 购物车ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 选中状态：0-未选中 1-已选中
     */
    private Integer checked;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 非数据库字段，商品名称
     */
    @TableField(exist = false)
    private String productName;
    
    /**
     * 非数据库字段，商品价格
     */
    @TableField(exist = false)
    private BigDecimal productPrice;
    
    /**
     * 非数据库字段，商品库存
     */
    @TableField(exist = false)
    private Integer productStock;
    
    /**
     * 非数据库字段，商品图片
     */
    @TableField(exist = false)
    private String productImages;
    
    /**
     * 非数据库字段，商品总价
     */
    @TableField(exist = false)
    private BigDecimal totalPrice;
    
    /**
     * 非数据库字段，商品状态：0-下架 1-正常
     */
    @TableField(exist = false)
    private Integer productStatus;
    
    /**
     * 非数据库字段，商品规格信息
     */
    @TableField(exist = false)
    private String specifications;
    
    /**
     * 非数据库字段，是否可购买（商品上架且库存足够）
     */
    @TableField(exist = false)
    private Boolean canBuy;
    
    /**
     * 非数据库字段，序列化ID
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}