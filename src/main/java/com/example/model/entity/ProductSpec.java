package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * 商品规格表
 *
 * @TableName product_spec
 */
@TableName(value = "product_spec")
@Data
public class ProductSpec implements Serializable {
    /**
     * 规格ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 规格值，JSON格式
     */
    private String specValues;

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
}