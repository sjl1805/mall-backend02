package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户收货地址表
 * 该实体类存储用户的收货地址信息，支持默认地址设置和地址标签
 * 是电商系统订单配送的基础数据
 *
 * @TableName user_address
 */
@TableName(value = "user_address")
@Data
public class UserAddress implements Serializable {
    /**
     * 非数据库字段，序列化ID
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    private Long userId;
    /**
     * 默认地址状态：0-非默认 1-默认
     */
    private Integer isDefault;
    /**
     *
     */
    private Integer isDefaultTrue;
    /**
     *
     */
    private String receiverName;
    /**
     *
     */
    private String receiverPhone;
    /**
     *
     */
    private String province;
    /**
     *
     */
    private String city;
    /**
     *
     */
    private String district;
    /**
     *
     */
    private String detailAddress;
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
     * 邮政编码
     */
    private String postalCode;
    /**
     * 地址标签：家、公司、学校等
     */
    private String tag;
    /**
     * 经度
     */
    @TableField(exist = false)
    private Double longitude;
    /**
     * 纬度
     */
    @TableField(exist = false)
    private Double latitude;
    /**
     * 地址完整性：0-不完整 1-完整
     */
    @TableField(exist = false)
    private Integer addressCompleteness;
    /**
     * 收货频率：记录该地址被使用的次数
     */
    @TableField(exist = false)
    private Integer useFrequency;
    /**
     * 是否为最近使用的地址
     */
    @TableField(exist = false)
    private Boolean isRecent;
    /**
     * 完整地址（省市区+详细地址）
     */
    @TableField(exist = false)
    private String fullAddress;

    /**
     * 获取完整地址
     */
    public String getFullAddress() {
        return this.province + this.city + this.district + this.detailAddress;
    }
}