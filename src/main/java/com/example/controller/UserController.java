package com.example.controller;

import com.example.common.api.CommonResult;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.common.api.ResultCode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;




@Tag(name = "用户管理", description = "用户注册登录及基本信息管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UsersService usersService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public CommonResult<Boolean> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        boolean result = usersService.register(userRegisterDTO);
        return result ? CommonResult.success(true) : 
                      CommonResult.failed(ResultCode.USERNAME_EXISTED);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public CommonResult<Users> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            Users user = usersService.login(userLoginDTO);
            return CommonResult.success(user);
        } catch (RuntimeException e) {
            return CommonResult.failed(ResultCode.LOGIN_FAILED, e.getMessage());
        }
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/{userId}")
    public CommonResult<Users> getUserInfo(@PathVariable Long userId) {
        Users user = usersService.getUserById(userId);
        return user != null ? CommonResult.success(user) : 
                            CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "用户分页列表")
    @GetMapping("/list")
    public CommonResult<IPage<Users>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Users> pageParam = new Page<>(page, size);
        return CommonResult.success(usersService.selectPage(pageParam));
    }
} 