package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "用户收货地址实体")
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
    @Schema(description = "地址ID", example = "1")
    private Long id;
    /**
     *
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;
    /**
     * 默认地址状态：0-非默认 1-默认
     */
    @Schema(description = "默认地址状态：0-非默认 1-默认", example = "1")
    private Integer isDefault;
    /**
     *
     */
    @Schema(description = "是否默认地址（布尔类型）", example = "true")
    private Integer isDefaultTrue;
    /**
     *
     */
    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;
    /**
     *
     */
    @Schema(description = "收货人电话", example = "13800138000")
    private String receiverPhone;
    /**
     *
     */
    @Schema(description = "省份", example = "北京市")
    private String province;
    /**
     *
     */
    @Schema(description = "城市", example = "北京市")
    private String city;
    /**
     *
     */
    @Schema(description = "区/县", example = "海淀区")
    private String district;
    /**
     *
     */
    @Schema(description = "详细地址", example = "清华大学xx号楼")
    private String detailAddress;
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
     * 邮政编码
     */
    @Schema(description = "邮政编码", example = "100084")
    private String postalCode;
    /**
     * 地址标签：家、公司、学校等
     */
    @Schema(description = "地址标签", example = "家")
    private String tag;
    /**
     * 经度
     */
    @TableField(exist = false)
    @Schema(description = "经度", example = "116.3252")
    private Double longitude;
    /**
     * 纬度
     */
    @TableField(exist = false)
    @Schema(description = "纬度", example = "39.9839")
    private Double latitude;
    /**
     * 地址完整性：0-不完整 1-完整
     */
    @TableField(exist = false)
    @Schema(description = "地址完整性：0-不完整 1-完整", example = "1")
    private Integer addressCompleteness;
    /**
     * 收货频率：记录该地址被使用的次数
     */
    @TableField(exist = false)
    @Schema(description = "收货频率", example = "5")
    private Integer useFrequency;
    /**
     * 是否为最近使用的地址
     */
    @TableField(exist = false)
    @Schema(description = "是否为最近使用的地址", example = "true")
    private Boolean isRecent;
    /**
     * 完整地址（省市区+详细地址）
     */
    @TableField(exist = false)
    @Schema(description = "完整地址", example = "北京市北京市海淀区清华大学xx号楼")
    private String fullAddress;

    /**
     * 获取完整地址
     */
    public String getFullAddress() {
        return this.province + this.city + this.district + this.detailAddress;
    }
}