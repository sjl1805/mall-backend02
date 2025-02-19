package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.product.ProductsDTO;
import com.example.model.dto.product.ProductsPageDTO;
import com.example.model.entity.Products;
import com.example.service.ProductsService;
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

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "商品管理接口")
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping
    @Operation(summary = "分页查询商品", description = "根据条件分页查询商品列表")
    @ApiResponse(responseCode = "200", description = "成功返回分页数据")
    public Result<IPage<Products>> listProducts(
            @Parameter(description = "分页查询参数") @Valid ProductsPageDTO queryDTO) {
        return Result.success(productsService.listProductsPage(queryDTO));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建商品", description = "添加新的商品信息")
    @ApiResponse(responseCode = "201", description = "商品创建成功")
    public Result<Boolean> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "商品数据",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductsDTO.class))
            )
            @Valid @RequestBody ProductsDTO productsDTO) {
        return Result.success(productsService.addProduct(productsDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新商品", description = "修改指定商品信息")
    @ApiResponse(responseCode = "200", description = "商品更新成功")
    public Result<Boolean> updateProduct(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long id,
            @Valid @RequestBody ProductsDTO productsDTO) {
        productsDTO.setId(id);
        return Result.success(productsService.updateProduct(productsDTO));
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "调整库存", description = "增加或减少商品库存")
    @ApiResponse(responseCode = "200", description = "库存调整成功")
    public Result<Boolean> adjustStock(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long id,
            @Parameter(description = "库存变化量（正数增加，负数减少）", example = "10")
            @RequestParam @NotNull Integer delta) {
        return Result.success(productsService.adjustStock(id, delta));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "切换商品状态", description = "启用/禁用商品")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    public Result<Boolean> switchStatus(
            @Parameter(description = "商品ID", example = "1") @PathVariable @Min(1) Long id,
            @Parameter(description = "新状态 0-禁用 1-启用", example = "1")
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(productsService.switchStatus(id, status));
    }

    @GetMapping("/new-arrivals")
    @Operation(summary = "获取新品推荐", description = "根据分类获取最新上架商品")
    @ApiResponse(responseCode = "200", description = "成功返回新品列表")
    public Result<List<Products>> getNewArrivals(
            @Parameter(description = "分类ID（0表示全部）", example = "0")
            @RequestParam(defaultValue = "0") Long categoryId,
            @Parameter(description = "返回数量", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) Integer limit) {
        return Result.success(productsService.getNewArrivals(categoryId, limit));
    }
} 