package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.users.UserBehaviorDTO;
import com.example.model.entity.UserBehavior;
import com.example.service.UserBehaviorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户行为分析控制器
 * 
 * @author 31815
 * @description 提供用户行为分析管理功能，包含：
 *              1. 行为记录与追踪
 *              2. 用户画像分析
 *              3. 行为数据统计
 * @createDate 2025-02-18 23:43:52
 */
@RestController
@RequestMapping("/users/{userId}/behaviors")
@RequiredArgsConstructor
@Tag(name = "UserBehavior", description = "用户行为分析接口体系")
public class UserBehaviorController {

    private final UserBehaviorService userBehaviorService;

    /**
     * 记录用户行为（带重复校验）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param behaviorDTO 行为信息：
     *                   - productId: 商品ID（必须）
     *                   - behaviorType: 行为类型（0-浏览，1-收藏，2-加购）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - BEHAVIOR_DUPLICATE(3001): 重复行为记录
     */
    @PostMapping
    @Operation(summary = "记录用户行为", description = "记录用户的浏览、收藏等行为接口")
    @ApiResponse(responseCode = "200", description = "行为记录成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复行为记录")
    public Result<Boolean> recordBehavior(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "行为信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserBehaviorDTO.class))
            )
            @Valid @RequestBody UserBehaviorDTO behaviorDTO) {
        behaviorDTO.setUserId(userId);
        return Result.success(userBehaviorService.recordBehavior(behaviorDTO));
    }

    /**
     * 获取近期行为（带缓存）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param limit 查询数量（默认10，最大100）
     * @return 行为记录列表（按时间倒序）
     * @implNote 结果缓存优化，有效期15分钟
     */
    @GetMapping("/recent")
    @Operation(summary = "近期行为查询", description = "获取用户最近行为记录接口")
    @ApiResponse(responseCode = "200", description = "成功返回行为列表")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    public Result<List<UserBehaviorDTO>> getRecentBehaviors(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "查询数量", example = "10", schema = @Schema(maximum = "100")) 
            @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(userBehaviorService.getRecentBehaviors(userId, limit));
    }

    /**
     * 获取用户权重分析
     * @param userId 用户ID（路径参数，必须大于0）
     * @return 权重指标：
     *         - activeScore: 活跃度评分
     *         - preference: 商品偏好分布
     *         - behaviorFrequency: 行为频率统计
     * @implNote 结果缓存优化，有效期1小时
     */
    @GetMapping("/weight")
    @Operation(summary = "用户画像分析", description = "计算用户行为权重值接口")
    @ApiResponse(responseCode = "200", description = "成功返回权重信息")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    public Result<Map<String, Object>> getUserWeight(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId) {
        return Result.success(userBehaviorService.getUserWeight(userId));
    }

    /**
     * 获取行为分布统计
     * @param days 统计天数（默认7天，最大30天）
     * @return 按行为类型分类的统计结果
     * @implNote 结果缓存优化，有效期2小时
     */
    @GetMapping("/distribution")
    @Operation(summary = "行为分布统计", description = "分析用户行为分布情况接口")
    @ApiResponse(responseCode = "200", description = "成功返回统计结果")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    public Result<List<Map<String, Object>>> getBehaviorDistribution(
            @Parameter(description = "统计天数", example = "7", schema = @Schema(maximum = "30"))
            @RequestParam(defaultValue = "7") Integer days) {
        return Result.success(userBehaviorService.getBehaviorDistribution(days));
    }

    /**
     * 批量记录行为（事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param behaviors 行为列表（最多100条）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - BEHAVIOR_DUPLICATE(3001): 存在重复行为记录
     */
    @PostMapping("/batch")
    @Operation(summary = "批量行为记录", description = "批量导入用户行为数据接口")
    @ApiResponse(responseCode = "200", description = "批量记录成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "413", description = "超出批量处理限制")
    public Result<Boolean> batchRecordBehaviors(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @RequestBody List<@Valid UserBehaviorDTO> behaviors) {
        behaviors.forEach(b -> b.setUserId(userId));
        return Result.success(userBehaviorService.batchRecordBehaviors(behaviors));
    }
} 