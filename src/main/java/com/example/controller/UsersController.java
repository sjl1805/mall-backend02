package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.entity.Users;
import com.example.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 
 * 提供用户注册、登录及用户信息管理相关API接口
 */
@Tag(name = "Users", description = "用户注册登录及基本信息管理")
@RestController
@RequestMapping("/users")
public class UsersController {

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

    @Operation(summary = "批量删除用户")
    @DeleteMapping("/batch")
    public CommonResult<Boolean> batchDeleteUsers(@RequestBody List<Long> userIds) {
        boolean result = usersService.batchDeleteUsers(userIds);
        return result ? CommonResult.success(true) :
                CommonResult.failed(ResultCode.FAILED);
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

    @Operation(summary = "更新用户手机号")
    @PutMapping("/{userId}/phone")
    public CommonResult<Boolean> updateUserPhone(
            @PathVariable Long userId,
            @RequestBody Map<String, String> phoneMap) {
        String newPhone = phoneMap.get("newPhone");
        String verifyCode = phoneMap.get("verifyCode");
        boolean result = usersService.updateUserPhone(userId, newPhone, verifyCode);
        return result ? CommonResult.success(true) :
                CommonResult.failed(ResultCode.FAILED, "手机号更新失败，可能已被其他用户使用");
    }
} 