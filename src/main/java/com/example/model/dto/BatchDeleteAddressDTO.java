package com.example.model.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量删除收货地址数据传输对象
 */
@Data
public class BatchDeleteAddressDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 收货地址ID列表
     */
    @NotEmpty(message = "收货地址ID列表不能为空")
    private List<Long> addressIds;
} 