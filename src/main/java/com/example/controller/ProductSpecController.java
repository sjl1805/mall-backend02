package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.product.ProductSpecDTO;
import com.example.model.entity.ProductSpec;
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

@RestController
@RequestMapping("/products/{productId}/specs")
@RequiredArgsConstructor
@Tag(name = "ProductSpec", description = "商品规格管理接口")
public class ProductSpecController {

    private final ProductSpecService productSpecService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "批量创建规格", description = "为商品添加多个规格项")
    @ApiResponse(responseCode = "201", description = "规格创建成功")
    public Result<Boolean> createSpecs(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "规格列表",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductSpecDTO.class))
            )
            @RequestBody @NotEmpty List<@Valid ProductSpecDTO> specs) {
        return Result.success(productSpecService.batchCreateSpecs(productId, specs));
    }

    @GetMapping
    @Operation(summary = "获取商品规格", description = "查询指定商品的所有规格信息")
    @ApiResponse(responseCode = "200", description = "成功返回规格列表")
    public Result<List<ProductSpec>> getSpecs(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId) {
        return Result.success(productSpecService.getSpecsByProductId(productId));
    }

    @PatchMapping("/{specId}")
    @Operation(summary = "更新规格值", description = "修改指定规格的可选值")
    @ApiResponse(responseCode = "200", description = "规格更新成功")
    public Result<Boolean> updateSpecValues(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @Parameter(description = "规格ID", example = "1") @PathVariable @Min(1) Long specId,
            @Parameter(description = "新的规格值（逗号分隔）", example = "黑色,白色")
            @RequestParam @NotNull String specValues) {
        return Result.success(productSpecService.updateSpecValues(productId, specId, specValues));
    }

    @GetMapping("/count")
    @Operation(summary = "获取规格数量", description = "查询商品的规格总数")
    @ApiResponse(responseCode = "200", description = "成功返回数量")
    public Result<Integer> getSpecCount(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId) {
        return Result.success(productSpecService.getSpecCount(productId));
    }
} 