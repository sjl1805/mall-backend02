package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户行为记录表
 * 该实体类记录用户在电商平台的各种行为数据，用于分析用户偏好和行为模式
 * 是个性化推荐和精准营销的数据基础
 *
 * @TableName user_behavior
 */
@TableName(value = "user_behavior")
@Data
@Schema(description = "用户行为记录实体")
public class UserBehavior implements Serializable {
    /**
     * 非数据库字段，序列化ID
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 行为记录ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "行为记录ID", example = "1")
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
     * 行为类型：1-浏览 2-收藏 3-购买
     */
    @Schema(description = "行为类型：1-浏览 2-收藏 3-购买", example = "1")
    private Integer behaviorType;
    /**
     * 行为时间
     */
    @Schema(description = "行为时间", example = "2023-01-01T12:10:30")
    private LocalDateTime behaviorTime;
    /**
     * 停留时长（秒）
     */
    @Schema(description = "停留时长（秒）", example = "120")
    private Integer duration;
    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-01-01T12:10:30")
    private LocalDateTime createTime;
    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "2023-01-01T12:10:30")
    private LocalDateTime updateTime;
    /**
     * 行为权重
     */
    @Schema(description = "行为权重", example = "0.8")
    private BigDecimal weight;
    /**
     * 来源渠道：1-PC网站 2-移动网站 3-APP 4-小程序 5-其他
     */
    @Schema(description = "来源渠道：1-PC网站 2-移动网站 3-APP 4-小程序 5-其他", example = "3")
    private Integer sourceChannel;
    /**
     * 行为附加数据（JSON格式）
     */
    @Schema(description = "行为附加数据（JSON格式）", example = "{\"referrer\":\"search\",\"keyword\":\"手机\"}")
    private String behaviorData;
    /**
     * 客户端IP
     */
    @Schema(description = "客户端IP", example = "192.168.1.100")
    private String clientIp;
    /**
     * 设备类型：1-PC 2-手机 3-平板 4-其他
     */
    @Schema(description = "设备类型：1-PC 2-手机 3-平板 4-其他", example = "2")
    private Integer deviceType;
    /**
     * 操作系统
     */
    @Schema(description = "操作系统", example = "iOS 16.2")
    private String osType;
    /**
     * 浏览器类型
     */
    @Schema(description = "浏览器类型", example = "Safari")
    private String browserType;
    /**
     * 地理位置-省
     */
    @TableField(exist = false)
    @Schema(description = "地理位置-省", example = "北京市")
    private String province;
    /**
     * 地理位置-市
     */
    @TableField(exist = false)
    @Schema(description = "地理位置-市", example = "海淀区")
    private String city;
    /**
     * 行为后的转化：0-无转化 1-加入购物车 2-购买 3-分享
     */
    @TableField(exist = false)
    @Schema(description = "行为后的转化：0-无转化 1-加入购物车 2-购买 3-分享", example = "1")
    private Integer conversion;
    /**
     * 转化时间
     */
    @TableField(exist = false)
    @Schema(description = "转化时间", example = "2023-01-01T12:15:30")
    private LocalDateTime conversionTime;
    /**
     * 转化耗时（秒）
     */
    @TableField(exist = false)
    @Schema(description = "转化耗时（秒）", example = "300")
    private Integer conversionDuration;
    /**
     * 推荐来源ID（如果是通过推荐访问）
     */
    @TableField(exist = false)
    @Schema(description = "推荐来源ID", example = "5001")
    private Long recommendSourceId;
    /**
     * 行为影响因子
     */
    @TableField(exist = false)
    @Schema(description = "行为影响因子", example = "0.75")
    private BigDecimal influenceFactor;
}