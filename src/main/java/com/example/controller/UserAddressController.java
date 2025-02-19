package com.example.controller;


import com.example.common.Result;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/addresses")
@RequiredArgsConstructor
@Tag(name = "UserAddress", description = "用户地址管理接口")
public class UserAddressController {

    private final UserAddressService userAddressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "添加地址", description = "用户添加新的收货地址")
    @ApiResponse(responseCode = "201", description = "地址添加成功")
    public Result<Boolean> addAddress(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "地址信息",
                required = true,
                content = @Content(schema = @Schema(implementation = UserAddress.class))
            )
            @Valid @RequestBody UserAddress address) {
        address.setUserId(userId);
        return Result.success(userAddressService.addAddress(address));
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "更新地址", description = "修改用户收货地址信息")
    @ApiResponse(responseCode = "200", description = "地址更新成功")
    public Result<Boolean> updateAddress(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "地址ID", example = "1") @PathVariable @Min(1) Long addressId,
            @Valid @RequestBody UserAddress address) {
        address.setId(addressId);
        address.setUserId(userId);
        return Result.success(userAddressService.updateAddress(userId, address));
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "删除地址", description = "移除用户收货地址")
    @ApiResponse(responseCode = "200", description = "地址删除成功")
    public Result<Boolean> deleteAddress(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "地址ID", example = "1") @PathVariable @Min(1) Long addressId) {
        return Result.success(userAddressService.deleteAddress(userId, addressId));
    }

    @GetMapping
    @Operation(summary = "地址列表", description = "获取用户所有收货地址")
    @ApiResponse(responseCode = "200", description = "成功返回地址列表")
    public Result<List<UserAddress>> listAddresses(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId) {
        return Result.success(userAddressService.listUserAddresses(userId));
    }

    @PatchMapping("/{addressId}/default")
    @Operation(summary = "设置默认地址", description = "设置用户默认收货地址")
    @ApiResponse(responseCode = "200", description = "默认地址设置成功")
    public Result<Boolean> setDefaultAddress(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "地址ID", example = "1") @PathVariable @Min(1) Long addressId) {
        return Result.success(userAddressService.setDefaultAddress(userId, addressId));
    }

    @GetMapping("/default")
    @Operation(summary = "获取默认地址", description = "查询用户的默认收货地址")
    @ApiResponse(responseCode = "200", description = "成功返回默认地址")
    public Result<UserAddress> getDefaultAddress(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId) {
        return Result.success(userAddressService.getDefaultAddress(userId));
    }

    @GetMapping("/{addressId}/validate")
    @Operation(summary = "验证地址有效性", description = "检查地址信息是否完整")
    @ApiResponse(responseCode = "200", description = "返回验证结果")
    public Result<Boolean> validateAddress(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "地址ID", example = "1") @PathVariable @Min(1) Long addressId) {
        return Result.success(userAddressService.validateAddress(addressId));
    }
} 