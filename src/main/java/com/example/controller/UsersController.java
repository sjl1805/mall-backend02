package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.exception.BusinessException;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserLoginDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.dto.PageDTO;
import com.example.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户管理控制器
 * 
 * @author 31815
 * @description 提供用户管理相关接口，包含：
 *              1. 用户认证（登录/登出/注册）
 *              2. 用户信息管理
 *              3. 用户状态控制
 *              4. 数据统计分析
 * @createDate 2025-02-18 23:43:44
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "用户管理接口体系")
public class UsersController {

    private final UsersService usersService;

    /**
     * 分页查询用户列表（管理端）
     * @param query 分页查询参数：
     *              - page: 当前页码（默认1）
     *              - size: 每页数量（默认10，最大100）
     *              - keyword: 搜索关键字（用户名/手机号）
     *              - role: 角色过滤（1-用户，2-商家，9-管理员）
     *              - status: 状态过滤（0-禁用，1-启用）
     * @return 分页结果包装对象，包含用户基本信息列表
     */
    @GetMapping
    @Operation(summary = "分页查询用户", description = "管理端用户分页查询接口")
    @ApiResponse(responseCode = "200", description = "查询成功，返回分页数据")
    public Result<IPage<UserDTO>> listUsers(
            @Parameter(hidden = true) @Valid PageDTO<UserDTO> query) {
        return Result.success(usersService.listUsersByPage(query));
    }

    /**
     * 用户登录认证
     * @param loginDTO 登录凭证：
     *                 - username: 用户名/手机号（必须）
     *                 - password: 密码（SHA256加密传输）
     * @return 包含以下信息的Map：
     *         - userInfo: 用户基本信息（ID、用户名、头像、角色）
     *         - token: JWT访问令牌（有效期2小时）
     * @throws BusinessException 可能异常：
     *         - USER_NOT_FOUND(1001): 用户不存在
     *         - PASSWORD_ERROR(1002): 密码错误
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户认证接口，成功返回访问令牌")
    @ApiResponse(responseCode = "200", description = "登录成功，返回用户信息和令牌")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "401", description = "用户名或密码错误")
    public Result<Map<String, Object>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "登录凭证",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserLoginDTO.class))
            )
            @Valid @RequestBody UserLoginDTO loginDTO) {
        return Result.success(usersService.login(loginDTO));
    }

    /**
     * 用户登出操作
     * @return 操作结果：
     *         - true: 登出成功
     * @implNote 实际业务需配合前端清除本地存储的token，
     *           服务端需实现token失效机制（如JWT黑名单或Redis存储）
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "使用户当前令牌失效")
    @ApiResponse(responseCode = "200", description = "登出成功")
    @ApiResponse(responseCode = "401", description = "未授权访问")
    public Result<Boolean> logout() {
        return Result.success(usersService.logout());
    }

    /**
     * 获取用户详细信息
     * @param id 用户ID（路径参数，必须大于0）
     * @return 用户详细信息实体
     * @throws BusinessException 可能异常：
     *         - USER_NOT_FOUND(1001): 用户不存在
     */
    @GetMapping("/{id}")
    @Operation(summary = "用户详情", description = "根据ID获取用户完整信息")
    @Parameter(name = "id", description = "用户ID", example = "123", required = true)
    @ApiResponse(responseCode = "200", description = "查询成功")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    public Result<UserDTO> getUserById(
            @PathVariable @Min(1) Long id) {
        return Result.success(usersService.getById(id));
    }

    /**
     * 用户注册接口
     * @param registerDTO 注册信息：
     *                   - username: 用户名（4-20位字母数字组合）
     *                   - password: 密码（8-20位，包含大小写和数字）
     *                   - phone: 手机号（符合中国手机号格式）
     * @return 注册结果：
     *         - userId: 新用户ID
     *         - username: 用户名
     *         - token: 自动登录令牌
     * @throws BusinessException 可能异常：
     *         - INVALID_PHONE_FORMAT(1003): 手机号格式错误
     *         - USERNAME_EXISTS(1004): 用户名已存在
     *         - REGISTER_ERROR(1005): 注册失败
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "用户注册", description = "新用户注册接口")
    @ApiResponse(responseCode = "201", description = "注册成功，返回新用户信息")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    public Result<Map<String, Object>> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "注册信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRegisterDTO.class))
            )
            @Valid @RequestBody UserRegisterDTO registerDTO) {
        return Result.success(usersService.registerUser(registerDTO));
    }

    /**
     * 更新用户基本信息
     * @param id 用户ID（路径参数）
     * @return 操作结果
     * @throws BusinessException 可能异常：
     *         - USER_NOT_FOUND(1001): 用户不存在
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "修改用户基本信息（头像、联系方式等）")
    @Parameter(name = "id", description = "用户ID", example = "123", required = true)
    @ApiResponse(responseCode = "200", description = "更新成功")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    public Result<Boolean> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        return Result.success(usersService.updateById(userDTO));
    }

    /**
     * 更新用户状态（管理端）
     * @param id 用户ID
     * @param status 新状态（0-禁用，1-启用）
     * @return 操作结果
     * @throws BusinessException 可能异常：
     *         - INVALID_STATUS(1006): 状态值不合法
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "状态管理", description = "启用/禁用用户账号")
    @Parameter(name = "id", description = "用户ID", example = "123", required = true)
    @Parameter(name = "status", description = "账号状态", schema = @Schema(allowableValues = {"0", "1"}), required = true)
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @ApiResponse(responseCode = "400", description = "非法状态值")
    public Result<Boolean> updateUserStatus(
            @PathVariable Long id,
            @RequestParam @Min(0) @Max(1) Integer status) {
        return Result.success(usersService.updateUserStatus(id, status));
    }

    /**
     * 删除用户账号（管理端）
     * @param id 用户ID
     * @return 操作结果
     * @implNote 实际业务中建议使用逻辑删除而非物理删除
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "永久删除用户记录（谨慎操作）")
    @Parameter(name = "id", description = "用户ID", example = "123", required = true)
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    public Result<Boolean> deleteUser(
            @PathVariable Long id) {
        return Result.success(usersService.removeById(id));
    }

    /**
     * 获取用户统计看板数据
     * @return 统计指标：
     *         - totalUsers: 总用户数
     *         - status_0: 禁用用户数
     *         - status_1: 启用用户数
     *         - role_1: 普通用户数
     *         - role_2: 商家数
     *         - role_9: 管理员数
     */
    @GetMapping("/statistics")
    @Operation(summary = "数据统计", description = "用户维度数据看板")
    @ApiResponse(responseCode = "200", description = "获取统计成功")
    public Result<Map<String, Integer>> getUserStatistics() {
        return Result.success(usersService.getUserStatistics());
    }
} 