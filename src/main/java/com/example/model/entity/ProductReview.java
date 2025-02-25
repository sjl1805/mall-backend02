package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "商品评价实体")
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
    @Schema(description = "评价ID", example = "1")
    private Long id;
    /**
     * 订单ID
     */
    @Schema(description = "订单ID", example = "1001")
    private Long orderId;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "2001")
    private Long userId;
    /**
     * 商品ID
     */
    @Schema(description = "商品ID", example = "3001")
    private Long productId;
    /**
     * 评分：1-5分
     */
    @Schema(description = "评分：1-5分", example = "5")
    private Integer rating;
    /**
     * 评价内容
     */
    @Schema(description = "评价内容", example = "商品质量很好，物流很快，非常满意")
    private String content;
    /**
     * 评价图片，JSON格式
     */
    @Schema(description = "评价图片，JSON格式", example = "[\"http://example.com/img1.jpg\",\"http://example.com/img2.jpg\"]")
    private String images;
    /**
     * 审核状态：0-待审核 1-已通过 2-已拒绝
     */
    @Schema(description = "审核状态：0-待审核 1-已通过 2-已拒绝", example = "1")
    private Integer status;
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
     * 非数据库字段，用户昵称
     */
    @TableField(exist = false)
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;
    /**
     * 非数据库字段，用户头像
     */
    @TableField(exist = false)
    @Schema(description = "用户头像", example = "http://example.com/avatars/user1.jpg")
    private String avatar;
    /**
     * 非数据库字段，商品名称
     */
    @TableField(exist = false)
    @Schema(description = "商品名称", example = "苹果手机 iPhone 14")
    private String productName;
    /**
     * 非数据库字段，商品主图
     */
    @TableField(exist = false)
    @Schema(description = "商品主图", example = "http://example.com/images/iphone14.jpg")
    private String productImage;
    /**
     * 非数据库字段，评价图片列表（解析后）
     */
    @TableField(exist = false)
    @Schema(description = "评价图片列表（解析后）")
    private List<String> imageList;
    /**
     * 非数据库字段，评价回复内容
     */
    @TableField(exist = false)
    @Schema(description = "评价回复内容", example = "感谢您的评价，我们会继续努力提升服务质量")
    private String replyContent;
    /**
     * 非数据库字段，评价回复时间
     */
    @TableField(exist = false)
    @Schema(description = "评价回复时间", example = "2023-01-02T10:15:30")
    private LocalDateTime replyTime;
    /**
     * 非数据库字段，有用数量
     */
    @TableField(exist = false)
    @Schema(description = "有用数量", example = "12")
    private Integer usefulCount;
    /**
     * 非数据库字段，购买规格信息
     */
    @TableField(exist = false)
    @Schema(description = "购买规格信息", example = "黑色,128GB")
    private String specifications;
}