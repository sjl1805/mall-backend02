package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.product.RecommendProductDTO;
import com.example.model.dto.product.RecommendProductPageDTO;
import com.example.model.entity.RecommendProduct;
import com.example.service.RecommendProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommends")
@RequiredArgsConstructor
@Tag(name = "Recommend", description = "推荐商品管理接口")
public class RecommendProductController {

    private final RecommendProductService recommendProductService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建推荐项", description = "添加新的商品推荐")
    @ApiResponse(responseCode = "201", description = "推荐创建成功")
    public Result<Boolean> createRecommend(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "推荐信息",
                required = true,
                content = @Content(schema = @Schema(implementation = RecommendProductDTO.class))
            )
            @Valid @RequestBody RecommendProductDTO recommendDTO) {
        return Result.success(recommendProductService.createRecommend(recommendDTO));
    }

    @GetMapping
    @Operation(summary = "分页查询推荐", description = "分页获取推荐商品列表")
    @ApiResponse(responseCode = "200", description = "成功返回推荐列表")
    public Result<IPage<RecommendProduct>> listRecommends(
            @Valid RecommendProductPageDTO queryDTO) {
        return Result.success(recommendProductService.listRecommendPage(queryDTO));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "更新推荐状态", description = "启用/禁用推荐项")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    public Result<Boolean> updateStatus(
            @Parameter(description = "推荐项ID", example = "1") @PathVariable @Min(1) Long id,
            @Parameter(description = "新状态：0-禁用 1-启用", example = "1") 
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(recommendProductService.updateRecommendStatus(id, status));
    }

    @PatchMapping("/{id}/sort")
    @Operation(summary = "调整推荐排序", description = "修改推荐项的显示顺序")
    @ApiResponse(responseCode = "200", description = "排序更新成功")
    public Result<Boolean> updateSort(
            @Parameter(description = "推荐项ID", example = "1") @PathVariable @Min(1) Long id,
            @Parameter(description = "新排序值", example = "5") 
            @RequestParam @NotNull @Min(0) Integer sort) {
        return Result.success(recommendProductService.updateRecommendSort(id, sort));
    }

    @GetMapping("/active")
    @Operation(summary = "获取有效推荐", description = "查询当前生效的推荐商品")
    @ApiResponse(responseCode = "200", description = "成功返回推荐列表")
    public Result<List<RecommendProduct>> getActiveRecommends(
            @Parameter(description = "推荐类型", example = "1") @RequestParam(required = false) Integer type) {
        return Result.success(recommendProductService.getActiveRecommends(type));
    }

    @GetMapping("/stats")
    @Operation(summary = "推荐统计信息", description = "获取各类型推荐商品数量统计")
    @ApiResponse(responseCode = "200", description = "成功返回统计结果")
    public Result<Map<Integer, Long>> getRecommendStats() {
        return Result.success(recommendProductService.getRecommendStats());
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "批量创建推荐", description = "批量添加多个推荐项")
    @ApiResponse(responseCode = "201", description = "批量创建成功")
    public Result<Boolean> batchCreateRecommends(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "推荐列表",
                required = true,
                content = @Content(schema = @Schema(implementation = RecommendProductDTO.class))
            )
            @RequestBody List<@Valid RecommendProductDTO> recommends) {
        return Result.success(recommendProductService.batchCreateRecommends(recommends));
    }
} 