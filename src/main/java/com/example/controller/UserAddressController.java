package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.UserAddress;
import com.example.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UserAddress", description = "用户收货地址的增删改查")
@RestController
@RequestMapping("/userAddress")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @Operation(summary = "根据用户ID查询收货地址")
    @GetMapping("/user/{userId}")
    public CommonResult<List<UserAddress>> getAddressesByUserId(@PathVariable Long userId) {
        List<UserAddress> addresses = userAddressService.selectByUserId(userId);
        return CommonResult.success(addresses);
    }

    @Operation(summary = "分页查询收货地址")
    @GetMapping("/list")
    public CommonResult<IPage<UserAddress>> getAddressList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<UserAddress> pageParam = new Page<>(page, size);
        return CommonResult.success(userAddressService.selectPage(pageParam));
    }

    @Operation(summary = "新增收货地址")
    @PostMapping("/add")
    public CommonResult<Boolean> addUserAddress(@Valid @RequestBody UserAddress userAddress) {
        boolean result = userAddressService.insertUserAddress(userAddress);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新收货地址")
    @PutMapping("/update")
    public CommonResult<Boolean> updateUserAddress(@Valid @RequestBody UserAddress userAddress) {
        boolean result = userAddressService.updateUserAddress(userAddress);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除收货地址")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteUserAddress(@PathVariable Long id) {
        boolean result = userAddressService.deleteUserAddress(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 