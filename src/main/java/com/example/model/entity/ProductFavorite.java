package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品收藏表
 * 该实体类表示用户对商品的收藏记录，支持收藏分类管理
 * 方便用户快速找到感兴趣的商品，也为个性化推荐提供数据支持
 *
 * @TableName product_favorite
 */
@TableName(value = "product_favorite")
@Data
public class ProductFavorite implements Serializable {
    /**
     * 收藏ID
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
     * 收藏夹ID（NULL表示未分类）
     */
    private Long folderId;

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
     * 非数据库字段，序列化ID
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
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
     * 非数据库字段，商品主图
     */
    @TableField(exist = false)
    private String productImage;
    
    /**
     * 非数据库字段，收藏夹名称
     */
    @TableField(exist = false)
    private String folderName;
    
    /**
     * 非数据库字段，商品状态
     */
    @TableField(exist = false)
    private Integer productStatus;
    
    /**
     * 非数据库字段，商品库存
     */
    @TableField(exist = false)
    private Integer productStock;
    
    /**
     * 非数据库字段，商品销量
     */
    @TableField(exist = false)
    private Integer productSales;
}