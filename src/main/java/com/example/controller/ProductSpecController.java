package com.example.controller;

import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.ProductSpec;
import com.example.service.ProductSpecService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@Tag(name = "商品规格管理", description = "商品规格的增删改查")
@RestController
@RequestMapping("/api/productSpec")
public class ProductSpecController {

    @Autowired
    private ProductSpecService productSpecService;

    @Operation(summary = "根据商品ID查询商品规格")
    @GetMapping("/product/{productId}")
    public CommonResult<List<ProductSpec>> getSpecsByProductId(@PathVariable Long productId) {
        List<ProductSpec> specs = productSpecService.selectByProductId(productId);
        return CommonResult.success(specs);
    }

    @Operation(summary = "分页查询商品规格")
    @GetMapping("/list")
    public CommonResult<IPage<ProductSpec>> getProductSpecList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<ProductSpec> pageParam = new Page<>(page, size);
        return CommonResult.success(productSpecService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询商品规格")
    @GetMapping("/{id}")
    public CommonResult<ProductSpec> getProductSpecById(@PathVariable Long id) {
        ProductSpec spec = productSpecService.selectById(id);
        return spec != null ? CommonResult.success(spec) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增商品规格")
    @PostMapping("/add")
    public CommonResult<Boolean> addProductSpec(@Valid @RequestBody ProductSpec productSpec) {
        boolean result = productSpecService.insertProductSpec(productSpec);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新商品规格")
    @PutMapping("/update")
    public CommonResult<Boolean> updateProductSpec(@Valid @RequestBody ProductSpec productSpec) {
        boolean result = productSpecService.updateProductSpec(productSpec);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除商品规格")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteProductSpec(@PathVariable Long id) {
        boolean result = productSpecService.deleteProductSpec(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 