package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 非数据库字段，规格值列表（反序列化后）
     */
    @TableField(exist = false)
    private List<String> valueList;

    /**
     * 非数据库字段，关联的SKU数量
     */
    @TableField(exist = false)
    private Integer skuCount;

    /**
     * 非数据库字段，是否允许删除
     */
    @TableField(exist = false)
    private Boolean allowDelete;
}