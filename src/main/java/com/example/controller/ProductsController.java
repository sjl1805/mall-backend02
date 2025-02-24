package com.example.controller;

import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.Products;
import com.example.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

@Tag(name = "商品管理", description = "商品的增删改查")
@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @Operation(summary = "根据商品名称查询商品")
    @GetMapping("/name/{name}")
    public CommonResult<List<Products>> getProductsByName(@PathVariable String name) {
        List<Products> products = productsService.selectByName(name);
        return CommonResult.success(products);
    }

    @Operation(summary = "分页查询商品")
    @GetMapping("/list")
    public CommonResult<IPage<Products>> getProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Products> pageParam = new Page<>(page, size);
        return CommonResult.success(productsService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询商品")
    @GetMapping("/{id}")
    public CommonResult<Products> getProductById(@PathVariable Long id) {
        Products product = productsService.selectById(id);
        return product != null ? CommonResult.success(product) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增商品")
    @PostMapping("/add")
    public CommonResult<Boolean> addProduct(@Valid @RequestBody Products product) {
        boolean result = productsService.insertProduct(product);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新商品")
    @PutMapping("/update")
    public CommonResult<Boolean> updateProduct(@Valid @RequestBody Products product) {
        boolean result = productsService.updateProduct(product);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除商品")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteProduct(@PathVariable Long id) {
        boolean result = productsService.deleteProduct(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 