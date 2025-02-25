package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品评价表
 *
 * @TableName product_review
 */
@TableName(value = "product_review")
@Data
public class ProductReview implements Serializable {
    /**
     * 非数据库字段，序列化ID
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 评价ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 评分：1-5分
     */
    private Integer rating;
    /**
     * 评价内容
     */
    private String content;
    /**
     * 评价图片，JSON格式
     */
    private String images;
    /**
     * 审核状态：0-待审核 1-已通过 2-已拒绝
     */
    private Integer status;
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
     * 非数据库字段，用户昵称
     */
    @TableField(exist = false)
    private String nickname;
    /**
     * 非数据库字段，用户头像
     */
    @TableField(exist = false)
    private String avatar;
    /**
     * 非数据库字段，商品名称
     */
    @TableField(exist = false)
    private String productName;
    /**
     * 非数据库字段，商品主图
     */
    @TableField(exist = false)
    private String productImage;
    /**
     * 非数据库字段，评价图片列表（解析后）
     */
    @TableField(exist = false)
    private List<String> imageList;
    /**
     * 非数据库字段，评价回复内容
     */
    @TableField(exist = false)
    private String replyContent;
    /**
     * 非数据库字段，评价回复时间
     */
    @TableField(exist = false)
    private LocalDateTime replyTime;
    /**
     * 非数据库字段，有用数量
     */
    @TableField(exist = false)
    private Integer usefulCount;
    /**
     * 非数据库字段，购买规格信息
     */
    @TableField(exist = false)
    private String specifications;
}