package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
     * 行为类型：1-浏览 2-收藏 3-购买
     */
    private Integer behaviorType;
    /**
     * 行为时间
     */
    private LocalDateTime behaviorTime;
    /**
     * 停留时长（秒）
     */
    private Integer duration;
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
     * 行为权重
     */
    private BigDecimal weight;
    /**
     * 来源渠道：1-PC网站 2-移动网站 3-APP 4-小程序 5-其他
     */
    private Integer sourceChannel;
    /**
     * 行为附加数据（JSON格式）
     */
    private String behaviorData;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 设备类型：1-PC 2-手机 3-平板 4-其他
     */
    private Integer deviceType;
    /**
     * 操作系统
     */
    private String osType;
    /**
     * 浏览器类型
     */
    private String browserType;
    /**
     * 地理位置-省
     */
    @TableField(exist = false)
    private String province;
    /**
     * 地理位置-市
     */
    @TableField(exist = false)
    private String city;
    /**
     * 行为后的转化：0-无转化 1-加入购物车 2-购买 3-分享
     */
    @TableField(exist = false)
    private Integer conversion;
    /**
     * 转化时间
     */
    @TableField(exist = false)
    private LocalDateTime conversionTime;
    /**
     * 转化耗时（秒）
     */
    @TableField(exist = false)
    private Integer conversionDuration;
    /**
     * 推荐来源ID（如果是通过推荐访问）
     */
    @TableField(exist = false)
    private Long recommendSourceId;
    /**
     * 行为影响因子
     */
    @TableField(exist = false)
    private BigDecimal influenceFactor;
}