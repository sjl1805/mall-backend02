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

@RestController
@RequestMapping("/users/{userId}/behaviors")
@RequiredArgsConstructor
@Tag(name = "UserBehavior", description = "用户行为管理接口")
public class UserBehaviorController {

    private final UserBehaviorService userBehaviorService;

    @PostMapping
    @Operation(summary = "记录用户行为", description = "记录用户的浏览、收藏等行为")
    @ApiResponse(responseCode = "200", description = "行为记录成功")
    public Result<Boolean> recordBehavior(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "行为信息",
                required = true,
                content = @Content(schema = @Schema(implementation = UserBehaviorDTO.class))
            )
            @Valid @RequestBody UserBehaviorDTO behaviorDTO) {
        behaviorDTO.setUserId(userId);
        return Result.success(userBehaviorService.recordBehavior(behaviorDTO));
    }

    @GetMapping("/recent")
    @Operation(summary = "获取近期行为", description = "查询用户最近的行为记录")
    @ApiResponse(responseCode = "200", description = "成功返回行为列表")
    public Result<List<UserBehavior>> getRecentBehaviors(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "查询数量", example = "10") @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(userBehaviorService.getRecentBehaviors(userId, limit));
    }

    @GetMapping("/weight")
    @Operation(summary = "获取用户权重", description = "计算用户行为权重值")
    @ApiResponse(responseCode = "200", description = "成功返回权重信息")
    public Result<Map<String, Object>> getUserWeight(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId) {
        return Result.success(userBehaviorService.getUserWeight(userId));
    }

    @GetMapping("/distribution")
    @Operation(summary = "行为分布统计", description = "统计各类行为的分布情况")
    @ApiResponse(responseCode = "200", description = "成功返回统计结果")
    public Result<List<Map<String, Object>>> getBehaviorDistribution(
            @Parameter(description = "统计天数", example = "7") @RequestParam(defaultValue = "7") Integer days) {
        return Result.success(userBehaviorService.getBehaviorDistribution(days));
    }

    @PostMapping("/batch")
    @Operation(summary = "批量记录行为", description = "批量记录用户行为数据")
    @ApiResponse(responseCode = "200", description = "批量记录成功")
    public Result<Boolean> batchRecordBehaviors(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @RequestBody List<@Valid UserBehaviorDTO> behaviors) {
        behaviors.forEach(b -> b.setUserId(userId));
        return Result.success(userBehaviorService.batchRecordBehaviors(behaviors));
    }
} 