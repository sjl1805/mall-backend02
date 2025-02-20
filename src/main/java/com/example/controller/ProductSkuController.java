package com.example.controller;

import com.example.common.Result;
import com.example.model.dto.product.ProductSkuDTO;
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

/**
 * 商品SKU管理控制器
 * 
 * @author 31815
 * @description 提供商品SKU全生命周期管理功能，包含：
 *              1. SKU创建与维护
 *              2. 库存管理
 *              3. 销售数据统计
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/products/{productId}/skus")
@RequiredArgsConstructor
@Tag(name = "ProductSku", description = "商品SKU管理接口体系")
public class ProductSkuController {

    private final ProductSkuService productSkuService;

    /**
     * 批量创建SKU（事务操作）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param skus SKU列表（最多50条）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - SKU_DUPLICATE(7001): 重复SKU属性组合
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "批量创建SKU", description = "为商品添加多个SKU项接口")
    @ApiResponse(responseCode = "201", description = "SKU创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复SKU属性")
    public Result<Boolean> createSkus(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "SKU列表",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductSku.class))
            )
            @RequestBody @NotEmpty List<@Valid ProductSkuDTO> skus) {
        return Result.success(productSkuService.batchCreateSkus(productId, skus));
    }

    /**
     * 获取商品SKU（带缓存）
     * @param productId 商品ID（路径参数，必须大于0）
     * @return SKU列表（按创建时间排序）
     * @implNote 结果缓存优化，有效期1小时
     */
    @GetMapping
    @Operation(summary = "商品SKU查询", description = "获取指定商品的所有SKU信息接口")
    @ApiResponse(responseCode = "200", description = "成功返回SKU列表")
    public Result<List<ProductSkuDTO>> getSkus(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId) {
        return Result.success(productSkuService.getSkusByProductId(productId));
    }

    /**
     * 调整库存（事务操作）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param skuId SKU ID（路径参数，必须大于0）
     * @param quantity 调整数量（允许正负值）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - SKU_NOT_FOUND(7002): SKU不存在
     *         - INSUFFICIENT_STOCK(7003): 库存不足
     */
    @PatchMapping("/{skuId}/stock")
    @Operation(summary = "库存调整", description = "修改指定SKU的库存数量接口")
    @ApiResponse(responseCode = "200", description = "库存调整成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "404", description = "SKU不存在")
    public Result<Boolean> adjustStock(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @Parameter(description = "SKU ID", example = "1", required = true)
            @PathVariable @Min(1) Long skuId,
            @Parameter(description = "调整数量", example = "-5", required = true)
            @RequestParam @NotNull Integer quantity) {
        return Result.success(productSkuService.adjustStock(productId, skuId, quantity));
    }

    /**
     * 增加销量（事务操作）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param skuId SKU ID（路径参数，必须大于0）
     * @param quantity 增加数量（必须大于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - SKU_NOT_FOUND(7002): SKU不存在
     */
    @PatchMapping("/{skuId}/sales")
    @Operation(summary = "销量更新", description = "增加指定SKU的销量接口")
    @ApiResponse(responseCode = "200", description = "销量更新成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "404", description = "SKU不存在")
    public Result<Boolean> increaseSales(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @Parameter(description = "SKU ID", example = "1", required = true)
            @PathVariable @Min(1) Long skuId,
            @Parameter(description = "增加数量", example = "10", required = true)
            @RequestParam @Min(1) Integer quantity) {
        return Result.success(productSkuService.increaseSales(productId, skuId, quantity));
    }

    /**
     * 获取SKU状态统计（带缓存）
     * @param productId 商品ID（路径参数，必须大于0）
     * @return 状态分布统计（按状态码分组）
     * @implNote 结果缓存优化，有效期2小时
     */
    @GetMapping("/stats")
    @Operation(summary = "SKU状态统计", description = "查询商品SKU的状态分布接口")
    @ApiResponse(responseCode = "200", description = "成功返回统计结果")
    public Result<Map<Integer, Long>> getSkuStats(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId) {
        return Result.success(productSkuService.getSkuStatusStats(productId));
    }

    /**
     * 批量更新主图（事务操作）
     * @param productId 商品ID（路径参数，必须大于0）
     * @param oldImage 旧图片路径（必须）
     * @param newImage 新图片路径（必须）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - IMAGE_UPDATE_FAILED(7004): 图片更新失败
     */
    @PutMapping("/images")
    @Operation(summary = "主图批量更新", description = "更新商品SKU的主图路径接口")
    @ApiResponse(responseCode = "200", description = "主图更新成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "500", description = "图片更新失败")
    public Result<Boolean> updateMainImage(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable @Min(1) Long productId,
            @Parameter(description = "旧图片路径", required = true)
            @RequestParam String oldImage,
            @Parameter(description = "新图片路径", required = true)
            @RequestParam String newImage) {
        return Result.success(productSkuService.updateMainImage(productId, oldImage, newImage));
    }
} 