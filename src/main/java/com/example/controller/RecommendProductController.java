package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.RecommendProduct;
import com.example.service.RecommendProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "RecommendProduct", description = "推荐商品的增删改查")
@RestController
@RequestMapping("/recommendProduct")
public class RecommendProductController {

    @Autowired
    private RecommendProductService recommendProductService;

    @Operation(summary = "根据用户ID查询推荐商品")
    @GetMapping("/user/{userId}")
    public CommonResult<List<RecommendProduct>> getProductsByUserId(@PathVariable Long userId) {
        List<RecommendProduct> products = recommendProductService.selectByUserId(userId);
        return CommonResult.success(products);
    }

    @Operation(summary = "分页查询推荐商品")
    @GetMapping("/list")
    public CommonResult<IPage<RecommendProduct>> getRecommendProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<RecommendProduct> pageParam = new Page<>(page, size);
        return CommonResult.success(recommendProductService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询推荐商品")
    @GetMapping("/{id}")
    public CommonResult<RecommendProduct> getRecommendProductById(@PathVariable Long id) {
        RecommendProduct product = recommendProductService.selectById(id);
        return product != null ? CommonResult.success(product) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增推荐商品")
    @PostMapping("/add")
    public CommonResult<Boolean> addRecommendProduct(@Valid @RequestBody RecommendProduct recommendProduct) {
        boolean result = recommendProductService.insertRecommendProduct(recommendProduct);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新推荐商品")
    @PutMapping("/update")
    public CommonResult<Boolean> updateRecommendProduct(@Valid @RequestBody RecommendProduct recommendProduct) {
        boolean result = recommendProductService.updateRecommendProduct(recommendProduct);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除推荐商品")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteRecommendProduct(@PathVariable Long id) {
        boolean result = recommendProductService.deleteRecommendProduct(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 