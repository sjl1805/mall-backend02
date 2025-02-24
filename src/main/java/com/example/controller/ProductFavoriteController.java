package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.ProductFavorite;
import com.example.service.ProductFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ProductFavorite", description = "商品收藏的增删改查")
@RestController
@RequestMapping("/productFavorite")
public class ProductFavoriteController {

    @Autowired
    private ProductFavoriteService productFavoriteService;

    @Operation(summary = "根据用户ID查询商品收藏")
    @GetMapping("/user/{userId}")
    public CommonResult<List<ProductFavorite>> getFavoritesByUserId(@PathVariable Long userId) {
        List<ProductFavorite> favorites = productFavoriteService.selectByUserId(userId);
        return CommonResult.success(favorites);
    }

    @Operation(summary = "分页查询商品收藏")
    @GetMapping("/list")
    public CommonResult<IPage<ProductFavorite>> getFavoriteList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<ProductFavorite> pageParam = new Page<>(page, size);
        return CommonResult.success(productFavoriteService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询商品收藏")
    @GetMapping("/{id}")
    public CommonResult<ProductFavorite> getFavoriteById(@PathVariable Long id) {
        ProductFavorite favorite = productFavoriteService.selectById(id);
        return favorite != null ? CommonResult.success(favorite) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增商品收藏")
    @PostMapping("/add")
    public CommonResult<Boolean> addProductFavorite(@Valid @RequestBody ProductFavorite productFavorite) {
        boolean result = productFavoriteService.insertProductFavorite(productFavorite);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新商品收藏")
    @PutMapping("/update")
    public CommonResult<Boolean> updateProductFavorite(@Valid @RequestBody ProductFavorite productFavorite) {
        boolean result = productFavoriteService.updateProductFavorite(productFavorite);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除商品收藏")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteProductFavorite(@PathVariable Long id) {
        boolean result = productFavoriteService.deleteProductFavorite(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 