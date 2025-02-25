package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "商品收藏实体")
public class ProductFavorite implements Serializable {
    /**
     * 非数据库字段，序列化ID
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 收藏ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "收藏ID", example = "1")
    private Long id;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;
    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "2001")
    private Long productId;
    /**
     * 收藏夹ID（NULL表示未分类）
     */
    @Schema(description = "收藏夹ID（NULL表示未分类）", example = "3001")
    private Long folderId;
    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-01T12:30:00")
    private LocalDateTime updateTime;
    /**
     * 非数据库字段，商品名称
     */
    @TableField(exist = false)
    @Schema(description = "商品名称", example = "苹果手机 iPhone 14")
    private String productName;

    /**
     * 非数据库字段，商品价格
     */
    @TableField(exist = false)
    @Schema(description = "商品价格", example = "5999.00")
    private BigDecimal productPrice;

    /**
     * 非数据库字段，商品主图
     */
    @TableField(exist = false)
    @Schema(description = "商品主图", example = "http://example.com/images/iphone14.jpg")
    private String productImage;

    /**
     * 非数据库字段，收藏夹名称
     */
    @TableField(exist = false)
    @Schema(description = "收藏夹名称", example = "我喜欢的商品")
    private String folderName;

    /**
     * 非数据库字段，商品状态
     */
    @TableField(exist = false)
    @Schema(description = "商品状态：0-下架 1-正常", example = "1")
    private Integer productStatus;

    /**
     * 非数据库字段，商品库存
     */
    @TableField(exist = false)
    @Schema(description = "商品库存", example = "100")
    private Integer productStock;

    /**
     * 非数据库字段，商品销量
     */
    @TableField(exist = false)
    @Schema(description = "商品销量", example = "500")
    private Integer productSales;
}