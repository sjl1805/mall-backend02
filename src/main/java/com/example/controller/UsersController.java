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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "用户管理接口")
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    @Operation(summary = "分页查询用户列表")
    public Result<IPage<Users>> listUsers(UserPageDTO query) {
        return Result.success(usersService.listUsersByPage(query));
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用账号密码进行登录认证")
    @ApiResponse(responseCode = "200", description = "登录成功返回用户信息和访问令牌")
    public Result<Map<String, Object>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "登录参数",
                required = true,
                content = @Content(schema = @Schema(implementation = UserLoginDTO.class))
            )
            @Valid @RequestBody UserLoginDTO loginDTO) {
        return Result.success(usersService.login(loginDTO));
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "使当前用户的访问令牌失效")
    @ApiResponse(responseCode = "200", description = "登出成功")
    public Result<Boolean> logout() {
        // 从安全上下文中获取当前用户
        return Result.success(usersService.logout());
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
    @Operation(summary = "用户注册", description = "注册新用户账号")
    @ApiResponse(responseCode = "201", description = "注册成功返回新用户信息")
    public Result<Map<String, Object>> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "注册信息",
                required = true,
                content = @Content(schema = @Schema(implementation = UserRegisterDTO.class))
            )
            @Valid @RequestBody UserRegisterDTO registerDTO) {
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
    @Operation(summary = "更新用户状态", description = "修改用户启用/禁用状态")
    @Parameter(name = "status", description = "用户状态 0-禁用 1-启用", example = "1")
    public Result<Boolean> updateUserStatus(
            @Parameter(description = "用户ID", example = "123") @PathVariable Long id,
            @RequestParam @Min(0) @Max(1) Integer status) {
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
    public Result<Map<String, Integer>> getUserStatistics() {
        return Result.success(usersService.getUserStatistics());
    }
} 