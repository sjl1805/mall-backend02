package com.example.controller;

import com.example.common.Result;
import com.example.model.entity.ProductSku;
import com.example.service.ProductSkuService;
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
import java.util.Map;

@RestController
@RequestMapping("/products/{productId}/skus")
@RequiredArgsConstructor
@Tag(name = "ProductSku", description = "商品SKU管理接口")
public class ProductSkuController {

    private final ProductSkuService productSkuService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "批量创建SKU", description = "为商品添加多个SKU项")
    @ApiResponse(responseCode = "201", description = "SKU创建成功")
    public Result<Boolean> createSkus(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "SKU列表",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductSku.class))
            )
            @RequestBody @NotEmpty List<@Valid ProductSku> skus) {
        return Result.success(productSkuService.batchCreateSkus(productId, skus));
    }

    @GetMapping
    @Operation(summary = "获取商品SKU", description = "查询指定商品的所有SKU信息")
    @ApiResponse(responseCode = "200", description = "成功返回SKU列表")
    public Result<List<ProductSku>> getSkus(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId) {
        return Result.success(productSkuService.getSkusByProductId(productId));
    }

    @PatchMapping("/{skuId}/stock")
    @Operation(summary = "调整库存", description = "修改指定SKU的库存数量")
    @ApiResponse(responseCode = "200", description = "库存调整成功")
    public Result<Boolean> adjustStock(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @Parameter(description = "SKU ID", example = "1") @PathVariable @Min(1) Long skuId,
            @Parameter(description = "调整数量（正数增加，负数减少）", example = "-5")
            @RequestParam @NotNull Integer quantity) {
        return Result.success(productSkuService.adjustStock(productId, skuId, quantity));
    }

    @PatchMapping("/{skuId}/sales")
    @Operation(summary = "增加销量", description = "增加指定SKU的销量")
    @ApiResponse(responseCode = "200", description = "销量更新成功")
    public Result<Boolean> increaseSales(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @Parameter(description = "SKU ID", example = "1") @PathVariable @Min(1) Long skuId,
            @Parameter(description = "增加数量", example = "10")
            @RequestParam @Min(1) Integer quantity) {
        return Result.success(productSkuService.increaseSales(productId, skuId, quantity));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取SKU状态统计", description = "查询商品SKU的状态分布")
    @ApiResponse(responseCode = "200", description = "成功返回统计结果")
    public Result<Map<Integer, Long>> getSkuStats(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId) {
        return Result.success(productSkuService.getSkuStatusStats(productId));
    }

    @PutMapping("/images")
    @Operation(summary = "批量更新主图", description = "更新商品SKU的主图路径")
    @ApiResponse(responseCode = "200", description = "主图更新成功")
    public Result<Boolean> updateMainImage(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long productId,
            @Parameter(description = "旧图片路径") @RequestParam String oldImage,
            @Parameter(description = "新图片路径") @RequestParam String newImage) {
        return Result.success(productSkuService.updateMainImage(productId, oldImage, newImage));
    }
} 