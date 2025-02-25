package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common. Result;
import com.example.common.ResultCode;
import com.example.model.entity.ProductSpec;
import com.example.service.ProductSpecService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ProductSpec", description = "商品规格的增删改查")
@RestController
@RequestMapping("/productSpec")
public class ProductSpecController {

    @Autowired
    private ProductSpecService productSpecService;

    @Operation(summary = "根据商品ID查询商品规格")
    @GetMapping("/product/{productId}")
    public  Result<List<ProductSpec>> getSpecsByProductId(@PathVariable Long productId) {
        List<ProductSpec> specs = productSpecService.selectByProductId(productId);
        return  Result.success(specs);
    }

    @Operation(summary = "分页查询商品规格")
    @GetMapping("/list")
    public  Result<IPage<ProductSpec>> getProductSpecList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<ProductSpec> pageParam = new Page<>(page, size);
        return  Result.success(productSpecService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询商品规格")
    @GetMapping("/{id}")
    public  Result<ProductSpec> getProductSpecById(@PathVariable Long id) {
        ProductSpec spec = productSpecService.selectById(id);
        return spec != null ?  Result.success(spec) :  Result.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增商品规格")
    @PostMapping("/add")
    public  Result<Boolean> addProductSpec(@Valid @RequestBody ProductSpec productSpec) {
        boolean result = productSpecService.insertProductSpec(productSpec);
        return result ?  Result.success(true) :  Result.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新商品规格")
    @PutMapping("/update")
    public  Result<Boolean> updateProductSpec(@Valid @RequestBody ProductSpec productSpec) {
        boolean result = productSpecService.updateProductSpec(productSpec);
        return result ?  Result.success(true) :  Result.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除商品规格")
    @DeleteMapping("/{id}")
    public  Result<Boolean> deleteProductSpec(@PathVariable Long id) {
        boolean result = productSpecService.deleteProductSpec(id);
        return result ?  Result.success(true) :  Result.failed(ResultCode.FAILED);
    }
} 

