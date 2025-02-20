package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.users.UserAddressDTO;
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

/**
 * 用户地址管理控制器
 * 
 * @author 31815
 * @description 提供用户地址全生命周期管理功能，包含：
 *              1. 地址增删改查
 *              2. 默认地址管理
 *              3. 地址有效性验证
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/users/{userId}/addresses")
@RequiredArgsConstructor
@Tag(name = "UserAddress", description = "用户地址管理接口体系")
public class UserAddressController {

    private final UserAddressService userAddressService;

    /**
     * 添加收货地址（带格式校验）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param address 地址信息：
     *               - recipient: 收件人（必须）
     *               - phone: 联系电话（符合格式）
     *               - region: 省市区（必须）
     *               - detail: 详细地址（必须）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - ADDRESS_INVALID(4001): 地址信息不完整
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "添加地址", description = "用户添加新的收货地址接口")
    @ApiResponse(responseCode = "201", description = "地址添加成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "地址信息不完整")
    public Result<Boolean> addAddress(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "地址信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserAddress.class))
            )
            @Valid @RequestBody UserAddressDTO address) {
        address.setUserId(userId);
        return Result.success(userAddressService.addAddress(address));
    }

    /**
     * 更新地址信息（带归属校验）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param addressId 地址ID（路径参数，必须大于0）
     * @param address 更新后的地址信息
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - ADDRESS_NOT_FOUND(4002): 地址不存在
     *         - ADDRESS_OWNER_MISMATCH(4003): 地址不属于当前用户
     */
    @PutMapping("/{addressId}")
    @Operation(summary = "更新地址", description = "修改用户收货地址信息接口")
    @ApiResponse(responseCode = "200", description = "地址更新成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "404", description = "地址不存在")
    public Result<Boolean> updateAddress(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "地址ID", example = "1", required = true)
            @PathVariable @Min(1) Long addressId,
            @Valid @RequestBody UserAddressDTO address) {
        address.setId(addressId);
        address.setUserId(userId);
        return Result.success(userAddressService.updateAddress(userId, address));
    }

    /**
     * 删除地址（带事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param addressId 地址ID（路径参数，必须大于0）
     * @return 操作结果
     * @implNote 如果删除的是默认地址，自动设置最新地址为默认
     */
    @DeleteMapping("/{addressId}")
    @Operation(summary = "删除地址", description = "移除用户收货地址接口")
    @ApiResponse(responseCode = "200", description = "地址删除成功")
    @ApiResponse(responseCode = "404", description = "地址不存在")
    public Result<Boolean> deleteAddress(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "地址ID", example = "1", required = true)
            @PathVariable @Min(1) Long addressId) {
        return Result.success(userAddressService.deleteAddress(userId, addressId));
    }

    /**
     * 获取地址列表（带缓存）
     * @param userId 用户ID（路径参数，必须大于0）
     * @return 地址列表（按最后使用时间倒序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping
    @Operation(summary = "地址列表查询", description = "获取用户所有收货地址接口")
    @ApiResponse(responseCode = "200", description = "成功返回地址列表")
    public Result<List<UserAddressDTO>> listAddresses(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId) {
        return Result.success(userAddressService.listUserAddresses(userId));
    }

    /**
     * 设置默认地址（事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param addressId 地址ID（路径参数，必须大于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - ADDRESS_NOT_FOUND(4002): 地址不存在
     */
    @PatchMapping("/{addressId}/default")
    @Operation(summary = "设置默认地址", description = "设置用户默认收货地址接口")
    @ApiResponse(responseCode = "200", description = "默认地址设置成功")
    @ApiResponse(responseCode = "404", description = "地址不存在")
    public Result<Boolean> setDefaultAddress(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "地址ID", example = "1", required = true)
            @PathVariable @Min(1) Long addressId) {
        return Result.success(userAddressService.setDefaultAddress(userId, addressId));
    }

    /**
     * 获取默认地址（带缓存）
     * @param userId 用户ID（路径参数，必须大于0）
     * @return 默认地址信息
     * @implNote 结果缓存优化，有效期1小时
     */
    @GetMapping("/default")
    @Operation(summary = "默认地址查询", description = "获取用户默认收货地址接口")
    @ApiResponse(responseCode = "200", description = "成功返回默认地址")
    @ApiResponse(responseCode = "404", description = "未设置默认地址")
    public Result<UserAddressDTO> getDefaultAddress(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId) {
        return Result.success(userAddressService.getDefaultAddress(userId));
    }

    /**
     * 验证地址有效性
     * @param userId 用户ID（路径参数，必须大于0）
     * @param addressId 地址ID（路径参数，必须大于0）
     * @return 验证结果：
     *         - true: 地址完整有效
     *         - false: 地址信息不完整
     */
    @GetMapping("/{addressId}/validate")
    @Operation(summary = "地址有效性验证", description = "检查地址信息是否完整接口")
    @ApiResponse(responseCode = "200", description = "返回验证结果")
    public Result<Boolean> validateAddress(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "地址ID", example = "1", required = true)
            @PathVariable @Min(1) Long addressId) {
        return Result.success(userAddressService.validateAddress(addressId));
    }
} 