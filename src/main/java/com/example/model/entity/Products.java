package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * 商品表
 *
 * @TableName products
 */
@Schema(description = "商品实体")
@TableName(value = "products")
@Data
public class Products implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "2001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 分类ID
     */
    @Schema(description = "商品分类ID", example = "1001")
    private Long categoryId;
    /**
     * 商品名称
     */
    @Schema(description = "商品名称", example = "华为Mate50")
    private String name;
    /**
     * 商品描述
     */
    @Schema(description = "商品详细描述", example = "5G旗舰手机，徕卡影像系统")
    private String description;
    /**
     * 价格
     */
    @Schema(description = "商品价格", example = "5999.00")
    private BigDecimal price;
    /**
     * 库存
     */
    @Schema(description = "商品库存", example = "100")
    private Integer stock;
    /**
     * 商品图片JSON数组
     */
    @Schema(description = "商品图片列表", example = "[\"/images/products/mate50_1.jpg\"]")
    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private List<String> images;
    /**
     * 创建时间（带时区）
     */
    @Schema(description = "创建时间", example = "2023-08-01T10:15:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @Schema(description = "更新时间", example = "2023-08-01T10:15:30")
    private LocalDateTime updateTime;
    /**
     * 商品状态：0-下架 1-上架
     */
    @Schema(description = "商品状态：0-下架 1-上架", example = "1")
    private Integer status;
}