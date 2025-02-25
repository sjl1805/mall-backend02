package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common. Result;
import com.example.common.ResultCode;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户收货地址控制器
 * <p>
 * 提供用户收货地址相关的API接口，包括增删改查、设置默认地址等操作
 */
@Tag(name = "UserAddress", description = "用户收货地址管理接口")
@RestController
@RequestMapping("/userAddress")
@Validated
@Slf4j
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @Operation(summary = "根据用户ID查询收货地址", description = "获取指定用户的所有收货地址")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<UserAddress>> getAddressesByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取用户收货地址列表请求: userId={}", userId);
        List<UserAddress> addresses = userAddressService.selectByUserId(userId);
        log.info("获取用户收货地址成功: userId={}, count={}", userId, addresses.size());
        return  Result.success(addresses);
    }

    @Operation(summary = "分页查询收货地址", description = "管理员分页查询所有用户收货地址")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<IPage<UserAddress>> getAddressList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询收货地址请求: page={}, size={}", page, size);
        IPage<UserAddress> pageParam = new Page<>(page, size);
        IPage<UserAddress> result = userAddressService.selectPage(pageParam);
        log.info("分页查询收货地址成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }

    @Operation(summary = "根据ID查询收货地址", description = "获取特定收货地址的详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userAddressService.checkAddressOwnership(authentication.principal.id, #id)")
    public  Result<UserAddress> getAddressById(
            @Parameter(description = "收货地址ID", required = true) @PathVariable Long id) {
        log.info("查询收货地址详情请求: id={}", id);
        UserAddress address = userAddressService.selectById(id);
        if (address != null) {
            log.info("查询收货地址详情成功: id={}", id);
            return  Result.success(address);
        } else {
            log.warn("查询收货地址详情失败: id={}, 地址不存在", id);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "收货地址不存在");
        }
    }

    @Operation(summary = "新增收货地址", description = "添加新的收货地址")
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userAddress.userId")
    public  Result<Boolean> addUserAddress(@Valid @RequestBody UserAddress userAddress) {
        log.info("新增收货地址请求: userId={}, receiverName={}", userAddress.getUserId(), userAddress.getReceiverName());
        
        // 验证地址有效性
        if (!userAddressService.validateAddress(userAddress)) {
            log.warn("新增收货地址失败: 地址信息无效, userId={}", userAddress.getUserId());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "地址信息无效");
        }
        
        boolean result = userAddressService.insertUserAddress(userAddress);
        if (result) {
            log.info("新增收货地址成功: userId={}, receiverName={}", userAddress.getUserId(), userAddress.getReceiverName());
            return  Result.success(true);
        } else {
            log.warn("新增收货地址失败: userId={}, receiverName={}", userAddress.getUserId(), userAddress.getReceiverName());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新收货地址", description = "修改现有收货地址信息")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @userAddressService.checkAddressOwnership(authentication.principal.id, #userAddress.id)")
    public  Result<Boolean> updateUserAddress(@Valid @RequestBody UserAddress userAddress) {
        log.info("更新收货地址请求: id={}", userAddress.getId());
        
        // 验证地址有效性
        if (!userAddressService.validateAddress(userAddress)) {
            log.warn("更新收货地址失败: 地址信息无效, id={}", userAddress.getId());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "地址信息无效");
        }
        
        boolean result = userAddressService.updateUserAddress(userAddress);
        if (result) {
            log.info("更新收货地址成功: id={}", userAddress.getId());
            return  Result.success(true);
        } else {
            log.warn("更新收货地址失败: id={}", userAddress.getId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除收货地址", description = "删除指定的收货地址")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userAddressService.checkAddressOwnership(authentication.principal.id, #id)")
    public  Result<Boolean> deleteUserAddress(
            @Parameter(description = "收货地址ID", required = true) @PathVariable Long id) {
        log.info("删除收货地址请求: id={}", id);
        boolean result = userAddressService.deleteUserAddress(id);
        if (result) {
            log.info("删除收货地址成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除收货地址失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "获取用户默认收货地址", description = "获取用户设置的默认收货地址")
    @GetMapping("/default/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<UserAddress> getDefaultAddress(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取用户默认收货地址请求: userId={}", userId);
        UserAddress defaultAddress = userAddressService.getDefaultAddress(userId);
        if (defaultAddress != null) {
            log.info("获取用户默认收货地址成功: userId={}, addressId={}", userId, defaultAddress.getId());
            return  Result.success(defaultAddress);
        } else {
            log.info("获取用户默认收货地址成功: userId={}, 无默认地址", userId);
            return  Result.success(null);
        }
    }
    
    @Operation(summary = "设置默认收货地址", description = "将指定地址设置为用户的默认收货地址")
    @PutMapping("/{userId}/default/{addressId}")
    @PreAuthorize("hasRole('ADMIN') or (authentication.principal.id == #userId and @userAddressService.checkAddressOwnership(#userId, #addressId))")
    public  Result<Boolean> setDefaultAddress(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "地址ID", required = true) @PathVariable Long addressId) {
        log.info("设置默认收货地址请求: userId={}, addressId={}", userId, addressId);
        
        // 验证地址是否属于用户
        if (!userAddressService.checkAddressOwnership(userId, addressId)) {
            log.warn("设置默认收货地址失败: 地址不属于该用户, userId={}, addressId={}", userId, addressId);
            return  Result.failed(ResultCode.FORBIDDEN, "地址不属于该用户");
        }
        
        boolean result = userAddressService.setDefaultAddress(userId, addressId);
        if (result) {
            log.info("设置默认收货地址成功: userId={}, addressId={}", userId, addressId);
            return  Result.success(true);
        } else {
            log.warn("设置默认收货地址失败: userId={}, addressId={}", userId, addressId);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "按地区查询收货地址", description = "根据省份和城市查询用户收货地址")
    @GetMapping("/region/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<UserAddress>> getAddressesByRegion(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "省份") @RequestParam String province,
            @Parameter(description = "城市") @RequestParam(required = false) String city) {
        log.info("按地区查询收货地址请求: userId={}, province={}, city={}", userId, province, city);
        List<UserAddress> addresses = userAddressService.selectByRegion(userId, province, city);
        log.info("按地区查询收货地址成功: userId={}, province={}, city={}, count={}", 
                userId, province, city, addresses.size());
        return  Result.success(addresses);
    }
    
    @Operation(summary = "获取用户常用地址", description = "获取用户使用频率最高的几个地址")
    @GetMapping("/frequent/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<List<UserAddress>> getFrequentlyUsedAddresses(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "限制数量") @RequestParam(required = false) Integer limit) {
        log.info("获取用户常用地址请求: userId={}, limit={}", userId, limit);
        List<UserAddress> addresses = userAddressService.getFrequentlyUsedAddresses(userId, limit);
        log.info("获取用户常用地址成功: userId={}, count={}", userId, addresses.size());
        return  Result.success(addresses);
    }
    
    @Operation(summary = "获取用户最近使用的地址", description = "获取用户最近一次使用的收货地址")
    @GetMapping("/recent/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<UserAddress> getRecentlyUsedAddress(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取用户最近使用的地址请求: userId={}", userId);
        UserAddress address = userAddressService.getRecentlyUsedAddress(userId);
        if (address != null) {
            log.info("获取用户最近使用的地址成功: userId={}, addressId={}", userId, address.getId());
            return  Result.success(address);
        } else {
            log.info("获取用户最近使用的地址成功: userId={}, 无最近使用地址", userId);
            return  Result.success(null);
        }
    }
    
    @Operation(summary = "批量删除收货地址", description = "批量删除指定的收货地址")
    @DeleteMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> batchDeleteAddresses(@RequestBody List<Long> ids) {
        log.info("批量删除收货地址请求: ids={}", ids);
        
        if (ids == null || ids.isEmpty()) {
            log.warn("批量删除收货地址失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "地址ID列表不能为空");
        }
        
        boolean result = userAddressService.batchDeleteAddresses(ids);
        if (result) {
            log.info("批量删除收货地址成功: ids={}", ids);
            return  Result.success(true);
        } else {
            log.warn("批量删除收货地址失败: ids={}", ids);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "检查地址是否属于用户", description = "验证指定地址是否属于该用户")
    @GetMapping("/check/{userId}/{addressId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public  Result<Boolean> checkAddressOwnership(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "地址ID", required = true) @PathVariable Long addressId) {
        log.info("检查地址所有权请求: userId={}, addressId={}", userId, addressId);
        boolean ownership = userAddressService.checkAddressOwnership(userId, addressId);
        log.info("检查地址所有权结果: userId={}, addressId={}, ownership={}", userId, addressId, ownership);
        return  Result.success(ownership);
    }
    
    @Operation(summary = "复制地址", description = "复制现有地址为新地址，便于快速创建相似地址")
    @PostMapping("/copy")
    @PreAuthorize("hasRole('ADMIN') or @userAddressService.checkAddressOwnership(authentication.principal.id, #params.get('addressId'))")
    public  Result<UserAddress> copyAddress(@RequestBody Map<String, Object> params) {
        Long addressId = Long.valueOf(params.get("addressId").toString());
        String newAddressName = params.get("newAddressName").toString();
        
        log.info("复制地址请求: addressId={}, newAddressName={}", addressId, newAddressName);
        
        if (newAddressName == null || newAddressName.trim().isEmpty()) {
            log.warn("复制地址失败: 新地址收件人姓名无效");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "收件人姓名不能为空");
        }
        
        UserAddress newAddress = userAddressService.copyAddress(addressId, newAddressName);
        if (newAddress != null) {
            log.info("复制地址成功: addressId={}, newAddressId={}", addressId, newAddress.getId());
            return  Result.success(newAddress);
        } else {
            log.warn("复制地址失败: addressId={}", addressId);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "检查地址是否可用", description = "验证地址信息的完整性和有效性")
    @PostMapping("/validate")
    public  Result<Boolean> validateAddress(@Valid @RequestBody UserAddress address) {
        log.info("检查地址有效性请求: userId={}", address.getUserId());
        boolean isValid = userAddressService.validateAddress(address);
        log.info("检查地址有效性结果: userId={}, isValid={}", address.getUserId(), isValid);
        return  Result.success(isValid);
    }
} 

