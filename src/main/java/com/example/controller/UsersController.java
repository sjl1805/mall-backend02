package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.entity.Users;
import com.example.service.UsersService;
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
 * 用户控制器
 * <p>
 * 提供用户注册、登录及用户信息管理相关API接口
 */
@Tag(name = "Users", description = "用户注册登录及基本信息管理")
@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Operation(summary = "用户注册", description = "新用户注册接口，用户名不能重复")
    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册请求: {}", userRegisterDTO.getUsername());
        boolean result = usersService.register(userRegisterDTO);
        if (result) {
            log.info("用户注册成功: {}", userRegisterDTO.getUsername());
            return Result.success(true);
        } else {
            log.warn("用户注册失败: {}", userRegisterDTO.getUsername());
            return Result.failed(ResultCode.USERNAME_EXISTED);
        }
    }

    @Operation(summary = "用户登录", description = "用户登录接口，返回用户信息")
    @PostMapping("/login")
    public Result<Users> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录请求: {}", userLoginDTO.getUsername());
        try {
            Users user = usersService.login(userLoginDTO);
            log.info("用户登录成功: {}", userLoginDTO.getUsername());
            return Result.success(user);
        } catch (RuntimeException e) {
            log.warn("用户登录失败: {}, 原因: {}", userLoginDTO.getUsername(), e.getMessage());
            return Result.failed(ResultCode.LOGIN_FAILED, e.getMessage());
        }
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户完整信息")
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public Result<Users> getUserInfo(
            @Parameter(description = "用户ID", required = true) 
            @PathVariable Long userId) {
        log.info("获取用户信息请求: {}", userId);
        Users user = usersService.getUserById(userId);
        return user != null ? Result.success(user) :
                Result.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "用户分页列表", description = "管理员查询用户列表，支持分页")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<Users>> getUserList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("获取用户列表请求: 页码={}, 每页数量={}", page, size);
        IPage<Users> pageParam = new Page<>(page, size);
        return CommonResult.success(usersService.selectPage(pageParam));
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/{userId}")
    public CommonResult<Boolean> updateUserInfo(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        boolean result = usersService.updateUserInfo(userId, userDTO);
        return result ? CommonResult.success(true) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{userId}")
    public CommonResult<Boolean> deleteUser(@PathVariable Long userId) {
        boolean result = usersService.deleteUser(userId);
        return result ? CommonResult.success(true) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "根据用户名查询用户")
    @GetMapping("/username/{username}")
    public CommonResult<Users> getUserByUsername(@PathVariable String username) {
        Users user = usersService.getUserByUsername(username);
        return user != null ? CommonResult.success(user) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "根据电话号码查询用户")
    @GetMapping("/phone/{phone}")
    public CommonResult<Users> getUserByPhone(@PathVariable String phone) {
        Users user = usersService.getUserByPhone(phone);
        return user != null ? CommonResult.success(user) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "根据邮箱查询用户")
    @GetMapping("/email/{email}")
    public CommonResult<Users> getUserByEmail(@PathVariable String email) {
        Users user = usersService.getUserByEmail(email);
        return user != null ? CommonResult.success(user) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "根据状态查询用户列表")
    @GetMapping("/status/{status}")
    public CommonResult<List<Users>> getUsersByStatus(@PathVariable Integer status) {
        List<Users> users = usersService.getUsersByStatus(status);
        return CommonResult.success(users);
    }

    @Operation(summary = "高级条件查询用户")
    @GetMapping("/search")
    public CommonResult<IPage<Users>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Users> pageParam = new Page<>(page, size);
        IPage<Users> result = usersService.getUsersByCondition(username, phone, status, pageParam);
        return CommonResult.success(result);
    }

    @Operation(summary = "更新用户状态")
    @PutMapping("/{userId}/status/{status}")
    public CommonResult<Boolean> updateUserStatus(
            @PathVariable Long userId,
            @PathVariable Integer status) {
        boolean result = usersService.updateUserStatus(userId, status);
        return result ? CommonResult.success(true) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "批量删除用户", description = "管理员批量删除用户")
    @DeleteMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> batchDeleteUsers(@RequestBody List<Long> userIds) {
        log.info("批量删除用户请求: {}", userIds);
        boolean result = usersService.batchDeleteUsers(userIds);
        if (result) {
            log.info("批量删除用户成功: {}", userIds);
            return CommonResult.success(true);
        } else {
            log.warn("批量删除用户失败: {}", userIds);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "检查用户名是否存在")
    @GetMapping("/check/{username}")
    public CommonResult<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = usersService.checkUsernameExists(username);
        return CommonResult.success(exists);
    }

    @Operation(summary = "重置用户密码")
    @PutMapping("/{userId}/reset-password")
    public CommonResult<Boolean> resetPassword(
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwordMap) {
        String newPassword = passwordMap.get("newPassword");
        boolean result = usersService.resetPassword(userId, newPassword);
        return result ? CommonResult.success(true) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/{userId}/change-password")
    public CommonResult<Boolean> changePassword(
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwordMap) {
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");
        boolean result = usersService.changePassword(userId, oldPassword, newPassword);
        return result ? CommonResult.success(true) :
                CommonResult.failed(ResultCode.PASSWORD_INCORRECT);
    }

    @Operation(summary = "更新用户角色")
    @PutMapping("/{userId}/role/{role}")
    public CommonResult<Boolean> updateUserRole(
            @PathVariable Long userId,
            @PathVariable Integer role) {
        boolean result = usersService.updateUserRole(userId, role);
        return result ? CommonResult.success(true) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "统计用户数量")
    @GetMapping("/count")
    public CommonResult<Integer> countUsers(
            @RequestParam(required = false) Integer status) {
        int count = usersService.countUsers(status);
        return CommonResult.success(count);
    }

    @Operation(summary = "获取用户基本信息（不含敏感信息）")
    @GetMapping("/{userId}/basic")
    public CommonResult<Users> getUserBasicInfo(@PathVariable Long userId) {
        Users user = usersService.getUserBasicInfo(userId);
        return user != null ? CommonResult.success(user) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "更新用户头像")
    @PutMapping("/{userId}/avatar")
    public CommonResult<Boolean> updateUserAvatar(
            @PathVariable Long userId,
            @RequestBody Map<String, String> avatarMap) {
        String avatarUrl = avatarMap.get("avatarUrl");
        boolean result = usersService.updateUserAvatar(userId, avatarUrl);
        return result ? CommonResult.success(true) :
                CommonResult.failed(ResultCode.USER_NOT_FOUND);
    }

    @Operation(summary = "更新用户手机号", description = "更新用户手机号，需要验证码")
    @PutMapping("/{userId}/phone")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public CommonResult<Boolean> updateUserPhone(
            @PathVariable Long userId,
            @RequestBody Map<String, String> phoneMap) {
        String newPhone = phoneMap.get("newPhone");
        String verifyCode = phoneMap.get("verifyCode");
        
        log.info("更新用户手机号请求: userId={}, newPhone={}", userId, newPhone);
        
        // 参数验证
        if (newPhone == null || !newPhone.matches("^1[3-9]\\d{9}$")) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "手机号格式不正确");
        }
        
        if (verifyCode == null || verifyCode.length() != 6) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "验证码格式不正确");
        }
        
        boolean result = usersService.updateUserPhone(userId, newPhone, verifyCode);
        if (result) {
            log.info("更新用户手机号成功: userId={}, newPhone={}", userId, newPhone);
            return CommonResult.success(true);
        } else {
            log.warn("更新用户手机号失败: userId={}, newPhone={}", userId, newPhone);
            return CommonResult.failed(ResultCode.FAILED, "手机号更新失败，可能已被其他用户使用");
        }
    }
} 