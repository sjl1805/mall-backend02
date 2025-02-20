package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.product.ProductSpecDTO;
import com.example.service.ProductSpecService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品规格管理控制器
 * 
 * @author 31815
 * @description 提供商品规格全生命周期管理功能，包含：
 *              1. 规格创建与维护
 *              2. 规格值管理
 *              3. 规格数据统计
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/products/{productId}/specs")
@RequiredArgsConstructor
@Tag(name = "ProductSpec", description = "商品规格管理接口体系")
public class ProductSpecController {

    private final ProductSpecService productSpecService;

    /**
     * 批量创建规格（事务操作）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param specs 规格列表（最多20条）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - SPEC_DUPLICATE(6001): 重复规格名称
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "批量创建规格", description = "为商品添加多个规格项接口")
    @ApiResponse(responseCode = "201", description = "规格创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复规格名称")
    public Result<Boolean> createSpecs(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "规格列表",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductSpecDTO.class))
            )
            @RequestBody @NotEmpty List<@Valid ProductSpecDTO> specs) {
        return Result.success(productSpecService.batchCreateSpecs(productId, specs));
    }

    /**
     * 获取商品规格（带缓存）
     * @param productId 商品ID（路径参数，必须大于0）
     * @return 规格列表（按创建时间排序）
     * @implNote 结果缓存优化，有效期1小时
     */
    @GetMapping
    @Operation(summary = "商品规格查询", description = "获取指定商品的所有规格信息接口")
    @ApiResponse(responseCode = "200", description = "成功返回规格列表")
    public Result<List<ProductSpecDTO>> getSpecs(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId) {
        return Result.success(productSpecService.getSpecsByProductId(productId));
    }

    /**
     * 更新规格值（带事务操作）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param specId 规格ID（路径参数，必须大于0）
     * @param specValues 新规格值（逗号分隔，至少包含一个值）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - SPEC_NOT_FOUND(6002): 规格不存在
     */
    @PatchMapping("/{specId}")
    @Operation(summary = "更新规格值", description = "修改指定规格的可选值接口")
    @ApiResponse(responseCode = "200", description = "规格更新成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "404", description = "规格不存在")
    public Result<Boolean> updateSpecValues(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @Parameter(description = "规格ID", example = "1", required = true)
            @PathVariable @Min(1) Long specId,
            @Parameter(description = "新的规格值", example = "黑色,白色", required = true)
            @RequestParam @NotNull String specValues) {
        return Result.success(productSpecService.updateSpecValues(productId, specId, specValues));
    }

    /**
     * 获取规格数量统计（带缓存）
     * @param productId 商品ID（路径参数，必须大于0）
     * @return 规格总数
     * @implNote 结果缓存优化，有效期2小时
     */
    @GetMapping("/count")
    @Operation(summary = "规格数量统计", description = "查询商品的规格总数接口")
    @ApiResponse(responseCode = "200", description = "成功返回数量")
    public Result<Integer> getSpecCount(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId) {
        return Result.success(productSpecService.getSpecCount(productId));
    }
} 