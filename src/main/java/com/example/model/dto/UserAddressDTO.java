package com.example.model.dto;

import com.example.model.entity.UserAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "用户地址数据传输对象")
public class UserAddressDTO {
    @Schema(description = "地址ID（更新时必填）", example = "456")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    private Long userId;

    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 20, message = "姓名最长20个字符")
    @Schema(description = "收货人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String receiverName;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED, example = "13812345678")
    private String receiverPhone;

    @NotBlank(message = "省份不能为空")
    @Size(max = 20, message = "省份最长20个字符")
    @Schema(description = "省份", requiredMode = Schema.RequiredMode.REQUIRED, example = "浙江省")
    private String province;

    @NotBlank(message = "城市不能为空")
    @Size(max = 20, message = "城市最长20个字符")
    @Schema(description = "城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "杭州市")
    private String city;

    @NotBlank(message = "区县不能为空")
    @Size(max = 20, message = "区县最长20个字符")
    @Schema(description = "区县", requiredMode = Schema.RequiredMode.REQUIRED, example = "西湖区")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 100, message = "地址最长100个字符")
    @Schema(description = "详细地址", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "文三路123号创业大厦")
    private String detailAddress;

    @Min(0)
    @Max(1)
    @Schema(description = "默认地址：0-否 1-是", defaultValue = "0", example = "1")
    private Integer isDefault = 0;

    public static UserAddressDTO fromEntity(UserAddress address) {
        UserAddressDTO dto = new UserAddressDTO();
        dto.setId(address.getId());
        dto.setUserId(address.getUserId());
        dto.setReceiverName(address.getReceiverName());
        dto.setReceiverPhone(address.getReceiverPhone());
        dto.setProvince(address.getProvince());
        dto.setCity(address.getCity());
        dto.setDistrict(address.getDistrict());
        dto.setDetailAddress(address.getDetailAddress());
        dto.setIsDefault(address.getIsDefault());
        return dto;
    }

    public UserAddress toEntity() {
        UserAddress address = new UserAddress();
        address.setId(this.id);
        address.setUserId(this.userId);
        address.setReceiverName(this.receiverName);
        address.setReceiverPhone(this.receiverPhone);
        address.setProvince(this.province);
        address.setCity(this.city);
        address.setDistrict(this.district);
        address.setDetailAddress(this.detailAddress);
        address.setIsDefault(this.isDefault);
        return address;
    }
} 