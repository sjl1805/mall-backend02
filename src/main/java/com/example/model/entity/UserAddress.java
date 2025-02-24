package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * 用户收货地址表
 *
 * @TableName user_address
 */
@TableName(value = "user_address")
@Data
public class UserAddress implements Serializable {
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
}