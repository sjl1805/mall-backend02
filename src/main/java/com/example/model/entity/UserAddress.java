package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;
/**
 * 用户收货地址表
 *
 * @TableName user_address
 */
@TableName(value = "user_address")
@Data
@Schema(description = "用户收货地址实体")
public class UserAddress {
    /**
     * 地址ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "地址ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    /**
     * 默认地址状态：0-非默认 1-默认
     */
    @Range(min = 0, max = 1, message = "默认状态参数不合法")
    @Schema(description = "默认地址 0-否 1-是", example = "0")
    private Integer isDefault = 0;

    /**
     * 虚拟生成列（自动处理）
     */
    @TableField(exist = false)
    @Schema(hidden = true)
    private Integer isDefaultTrue;

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 32, message = "姓名最长32个字符")
    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;

    /**
     * 收货人电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "联系电话", example = "13800138000")
    private String receiverPhone;

    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空")
    @Size(max = 50, message = "省份最长50个字符")
    @Schema(description = "省份", example = "广东省")
    private String province;

    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    @Size(max = 50, message = "城市最长50个字符")
    @Schema(description = "城市", example = "深圳市")
    private String city;

    /**
     * 区县
     */
    @NotBlank(message = "区县不能为空")
    @Size(max = 50, message = "区县最长50个字符")
    @Schema(description = "区县", example = "南山区")
    private String district;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址最长200个字符")
    @Schema(description = "详细地址", example = "科技园路123号")
    private String detailAddress;

    /**
     * 创建时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 00:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间（带时区）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-01-01 00:00:00")
    private LocalDateTime updateTime;
}