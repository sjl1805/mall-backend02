package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.product.RecommendProductDTO;
import com.example.model.dto.product.RecommendProductPageDTO;
import com.example.service.RecommendProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品推荐管理控制器
 * 
 * @author 31815
 * @description 提供商品推荐管理功能，包含：
 *              1. 推荐项管理
 *              2. 推荐状态控制
 *              3. 推荐效果分析
 * @createDate 2025-02-18 23:43:52
 */
@RestController
@RequestMapping("/recommends")
@RequiredArgsConstructor
@Tag(name = "Recommend", description = "商品推荐管理接口体系")
public class RecommendProductController {

    private final RecommendProductService recommendProductService;

    /**
     * 创建推荐项（带重复校验）
     * @param recommendDTO 推荐信息：
     *                   - productId: 商品ID（必须）
     *                   - recommendType: 推荐类型（1-首页推荐，2-同类推荐）
     *                   - sort: 排序值（默认0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - RECOMMEND_EXISTS(5001): 重复推荐同一商品
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建推荐项", description = "添加新的商品推荐接口")
    @ApiResponse(responseCode = "201", description = "推荐创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复推荐商品")
    public Result<Boolean> createRecommend(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "推荐信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RecommendProductDTO.class))
            )
            @Valid @RequestBody RecommendProductDTO recommendDTO) {
        return Result.success(recommendProductService.createRecommend(recommendDTO));
    }

    /**
     * 分页查询推荐（管理端）
     * @param queryDTO 分页参数：
     *                 - page: 当前页码（默认1）
     *                 - size: 每页数量（默认10，最大100）
     *                 - productName: 商品名称搜索
     *                 - status: 状态过滤（0-禁用，1-启用）
     * @return 分页结果包装对象
     */
    @GetMapping
    @Operation(summary = "分页查询推荐", description = "管理端推荐商品分页查询接口")
    @ApiResponse(responseCode = "200", description = "成功返回推荐列表")
    public Result<IPage<RecommendProductDTO>> listRecommends(
            @Valid RecommendProductPageDTO queryDTO) {
        return Result.success(recommendProductService.listRecommendPage(queryDTO));
    }

    /**
     * 更新推荐状态（事务操作）
     * @param id 推荐项ID（路径参数，必须大于0）
     * @param status 新状态（0-禁用，1-启用）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - RECOMMEND_NOT_FOUND(5002): 推荐项不存在
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "更新推荐状态", description = "启用/禁用推荐项接口")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @ApiResponse(responseCode = "404", description = "推荐项不存在")
    public Result<Boolean> updateStatus(
            @Parameter(description = "推荐项ID", example = "1", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "新状态", schema = @Schema(allowableValues = {"0", "1"}), required = true)
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(recommendProductService.updateRecommendStatus(id, status));
    }

    /**
     * 调整推荐排序（带事务操作）
     * @param id 推荐项ID（路径参数，必须大于0）
     * @param sort 新排序值（必须大于等于0）
     * @return 操作结果
     * @implNote 自动调整其他推荐项的排序值
     */
    @PatchMapping("/{id}/sort")
    @Operation(summary = "调整推荐排序", description = "修改推荐项的显示顺序接口")
    @ApiResponse(responseCode = "200", description = "排序更新成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    public Result<Boolean> updateSort(
            @Parameter(description = "推荐项ID", example = "1", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "新排序值", example = "5", required = true)
            @RequestParam @NotNull @Min(0) Integer sort) {
        return Result.success(recommendProductService.updateRecommendSort(id, sort));
    }

    /**
     * 获取有效推荐（带缓存）
     * @param type 推荐类型（可选，1-首页推荐，2-同类推荐）
     * @return 有效推荐列表（按排序值倒序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping("/active")
    @Operation(summary = "有效推荐查询", description = "获取当前生效的推荐商品接口")
    @ApiResponse(responseCode = "200", description = "成功返回推荐列表")
    public Result<List<RecommendProductDTO>> getActiveRecommends(
            @Parameter(description = "推荐类型", example = "1", schema = @Schema(allowableValues = {"1", "2"}))
            @RequestParam(required = false) Integer type) {
        return Result.success(recommendProductService.getActiveRecommends(type));
    }

    /**
     * 获取推荐统计信息（带缓存）
     * @return 统计指标：
     *         - 按类型统计推荐数量
     *         - 按状态统计推荐数量
     * @implNote 结果缓存优化，有效期2小时
     */
    @GetMapping("/stats")
    @Operation(summary = "推荐统计信息", description = "获取推荐商品数据看板接口")
    @ApiResponse(responseCode = "200", description = "成功返回统计结果")
    public Result<Map<Integer, Long>> getRecommendStats() {
        return Result.success(recommendProductService.getRecommendStats());
    }

    /**
     * 批量创建推荐（事务操作）
     * @param recommends 推荐列表（最多50条）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - BATCH_LIMIT_EXCEEDED(5003): 超出批量处理限制
     */
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "批量创建推荐", description = "批量添加多个推荐项接口")
    @ApiResponse(responseCode = "201", description = "批量创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "413", description = "超出批量处理限制")
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