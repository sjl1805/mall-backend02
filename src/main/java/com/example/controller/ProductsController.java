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

/**
 * 商品管理控制器
 * 
 * @author 31815
 * @description 提供商品全生命周期管理功能，包含：
 *              1. 商品增删改查
 *              2. 库存管理
 *              3. 商品状态管理
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "商品管理接口体系")
public class ProductsController {

    private final ProductsService productsService;

    /**
     * 分页查询商品（管理端）
     * @param queryDTO 分页参数：
     *                 - page: 当前页码（默认1）
     *                 - size: 每页数量（默认10，最大100）
     *                 - productName: 商品名称搜索
     *                 - status: 状态过滤（0-禁用，1-启用）
     * @return 分页结果包装对象
     * @implNote 结果缓存优化，有效期5分钟
     */
    @GetMapping
    @Operation(summary = "商品分页查询", description = "管理端商品分页查询接口")
    @ApiResponse(responseCode = "200", description = "成功返回分页数据")
    public Result<IPage<Products>> listProducts(
            @Parameter(description = "分页查询参数") @Valid ProductsPageDTO queryDTO) {
        return Result.success(productsService.listProductsPage(queryDTO));
    }

    /**
     * 创建商品（带事务操作）
     * @param productsDTO 商品数据：
     *                   - categoryId: 分类ID（必须）
     *                   - productName: 商品名称（必须）
     *                   - price: 基础价格（必须大于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - PRODUCT_EXISTS(8001): 商品名称重复
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "商品创建", description = "添加新的商品信息接口")
    @ApiResponse(responseCode = "201", description = "商品创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "商品名称重复")
    public Result<Boolean> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "商品数据",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductsDTO.class))
            )
            @Valid @RequestBody ProductsDTO productsDTO) {
        return Result.success(productsService.addProduct(productsDTO));
    }

    /**
     * 更新商品信息（带版本控制）
     * @param id 商品ID（路径参数，必须大于0）
     * @param productsDTO 更新后的商品数据
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - PRODUCT_NOT_FOUND(8002): 商品不存在
     */
    @PutMapping("/{id}")
    @Operation(summary = "商品信息更新", description = "修改指定商品信息接口")
    @ApiResponse(responseCode = "200", description = "商品更新成功")
    @ApiResponse(responseCode = "404", description = "商品不存在")
    public Result<Boolean> updateProduct(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody ProductsDTO productsDTO) {
        productsDTO.setId(id);
        return Result.success(productsService.updateProduct(productsDTO));
    }

    /**
     * 调整库存（事务操作）
     * @param id 商品ID（路径参数，必须大于0）
     * @param delta 库存变化量（允许正负值）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - INSUFFICIENT_STOCK(8003): 库存不足
     */
    @PatchMapping("/{id}/stock")
    @Operation(summary = "库存调整", description = "增加或减少商品库存接口")
    @ApiResponse(responseCode = "200", description = "库存调整成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "库存不足")
    public Result<Boolean> adjustStock(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "库存变化量", example = "10", required = true)
            @RequestParam @NotNull Integer delta) {
        return Result.success(productsService.adjustStock(id, delta));
    }

    /**
     * 切换商品状态（事务操作）
     * @param id 商品ID（路径参数，必须大于0）
     * @param status 新状态（0-禁用，1-启用）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - PRODUCT_NOT_FOUND(8002): 商品不存在
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "状态切换", description = "启用/禁用商品接口")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @ApiResponse(responseCode = "404", description = "商品不存在")
    public Result<Boolean> switchStatus(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "新状态", schema = @Schema(allowableValues = {"0", "1"}), required = true)
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(productsService.switchStatus(id, status));
    }

    /**
     * 获取新品推荐（带缓存）
     * @param categoryId 分类ID（0表示全部）
     * @param limit 返回数量（默认10，最大50）
     * @return 新品列表（按上架时间倒序）
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping("/new-arrivals")
    @Operation(summary = "新品推荐", description = "根据分类获取最新上架商品接口")
    @ApiResponse(responseCode = "200", description = "成功返回新品列表")
    public Result<List<Products>> getNewArrivals(
            @Parameter(description = "分类ID", example = "0", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") Long categoryId,
            @Parameter(description = "返回数量", example = "10", schema = @Schema(maximum = "50"))
            @RequestParam(defaultValue = "10") @Min(1) Integer limit) {
        return Result.success(productsService.getNewArrivals(categoryId, limit));
    }
} 