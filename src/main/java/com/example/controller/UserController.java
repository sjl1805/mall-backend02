package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.users.UserPageDTO;
import com.example.model.dto.users.UserRegisterDTO;
import com.example.model.dto.users.UserLoginDTO;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "用户相关操作接口")
public class UserController {

    private final UsersService usersService;

    @GetMapping
    @Operation(summary = "分页查询用户列表")
    public Result<IPage<Users>> listUsers(UserPageDTO query) {
        return Result.success(usersService.listUsersByPage(query));
    }

    @GetMapping("/login")
    @Operation(summary = "用户登录")
    public Result<Map<String, Object>> login(UserLoginDTO loginDTO) {
        return Result.success(usersService.login(loginDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    public Result<Users> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        System.out.println("获取用户详情");
        return Result.success(usersService.getById(id));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "注册新用户")
    public Result<Map<String, Object>> registerUser(
            @Parameter(description = "用户注册信息") @RequestBody UserRegisterDTO registerDTO) {
        return Result.success(usersService.registerUser(registerDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户信息")
    public Result<Boolean> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "用户更新信息") @RequestBody Users user) {
        user.setId(id);
        return Result.success(usersService.updateById(user));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "更新用户状态")
    public Result<Boolean> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "新状态：0-禁用 1-启用") @RequestParam Integer status) {
        return Result.success(usersService.updateUserStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Result<Boolean> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        return Result.success(usersService.removeById(id));
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取用户统计信息")
    public Result<Map<String, Object>> getUserStatistics() {
        return Result.success(usersService.getUserStatistics());
    }
} 