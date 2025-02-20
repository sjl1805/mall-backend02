package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户收货地址表
 *
 * @TableName user_address
 */
@Schema(description = "用户地址实体")
@TableName(value = "user_address")
@Data
public class UserAddress implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 地址ID
     */
    @Schema(description = "地址ID", example = "3001")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @Schema(description = "所属用户ID", example = "10001")
    private Long userId;
    /**
     * 默认地址状态：0-非默认 1-默认
     */
    @Schema(description = "默认地址标识：0-否 1-是", example = "1")
    private Integer isDefault;
    /**
     * 虚拟字段（数据库自动生成）
     */
    private Integer isDefaultTrue;
    /**
     * 收货人姓名
     */
    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;
    /**
     * 收货人电话
     */
    @Schema(description = "收货人电话", example = "13800000001")
    private String receiverPhone;
    /**
     * 省份
     */
    @Schema(description = "省份", example = "广东省")
    private String province;
    /**
     * 城市
     */
    @Schema(description = "城市", example = "深圳市")
    private String city;
    /**
     * 区县
     */
    @Schema(description = "区县", example = "南山区")
    private String district;
    /**
     * 详细地址
     */
    @Schema(description = "详细地址", example = "科技园科兴科学园B栋")
    private String detailAddress;
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
}