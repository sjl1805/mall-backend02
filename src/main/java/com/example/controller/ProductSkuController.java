package com.example.controller;

import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.ProductSku;
import com.example.service.ProductSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@Tag(name = "商品SKU管理", description = "商品SKU的增删改查")
@RestController
@RequestMapping("/api/productSku")
public class ProductSkuController {

    @Autowired
    private ProductSkuService productSkuService;

    @Operation(summary = "根据商品ID查询SKU")
    @GetMapping("/product/{productId}")
    public CommonResult<List<ProductSku>> getSkusByProductId(@PathVariable Long productId) {
        List<ProductSku> skus = productSkuService.selectByProductId(productId);
        return CommonResult.success(skus);
    }

    @Operation(summary = "分页查询SKU")
    @GetMapping("/list")
    public CommonResult<IPage<ProductSku>> getSkuList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<ProductSku> pageParam = new Page<>(page, size);
        return CommonResult.success(productSkuService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询SKU")
    @GetMapping("/{id}")
    public CommonResult<ProductSku> getSkuById(@PathVariable Long id) {
        ProductSku sku = productSkuService.selectById(id);
        return sku != null ? CommonResult.success(sku) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增SKU")
    @PostMapping("/add")
    public CommonResult<Boolean> addProductSku(@Valid @RequestBody ProductSku productSku) {
        boolean result = productSkuService.insertProductSku(productSku);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新SKU")
    @PutMapping("/update")
    public CommonResult<Boolean> updateProductSku(@Valid @RequestBody ProductSku productSku) {
        boolean result = productSkuService.updateProductSku(productSku);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除SKU")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteProductSku(@PathVariable Long id) {
        boolean result = productSkuService.deleteProductSku(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 