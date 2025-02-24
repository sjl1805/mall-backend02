package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.validator.constraints.Range;
/**
 * 商品表
 *
 * @TableName products
 */
@TableName(value = "products", autoResultMap = true)
@Data
@Schema(description = "商品实体")
public class Products {
    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "商品ID", example = "1")
    private Long id;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID", example = "1001")
    private Long categoryId;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称最长100个字符")
    @Schema(description = "商品名称", example = "华为Mate60 Pro")
    private String name;

    /**
     * 商品描述
     */
    @Schema(description = "商品描述", example = "旗舰智能手机")
    private String description;

    /**
     * 价格
     */
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格不能小于0.01")
    @Schema(description = "价格", example = "6999.00")
    private BigDecimal price;

    /**
     * 库存
     */
    @Min(value = 0, message = "库存不能小于0")
    @Schema(description = "库存数量", example = "100")
    private Integer stock = 0;

    /**
     * 商品图片JSON数组
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "商品图片URL列表", example = "[\"/images/p1.jpg\",\"/images/p2.jpg\"]")
    private List<String> images;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 00:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-01-01 00:00:00")
    private LocalDateTime updateTime;

    /**
     * 商品状态：0-下架 1-上架
     */
    @Range(min = 0, max = 1, message = "商品状态参数不合法")
    @Schema(description = "商品状态 0-下架 1-上架", example = "1")
    private Integer status = 1;
}