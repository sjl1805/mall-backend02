package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.ProductReview;
import com.example.service.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ProductReview", description = "商品评价的增删改查")
@RestController
@RequestMapping("/productReview")
public class ProductReviewController {

    @Autowired
    private ProductReviewService productReviewService;

    @Operation(summary = "根据商品ID查询评价")
    @GetMapping("/product/{productId}")
    public CommonResult<List<ProductReview>> getReviewsByProductId(@PathVariable Long productId) {
        List<ProductReview> reviews = productReviewService.selectByProductId(productId);
        return CommonResult.success(reviews);
    }

    @Operation(summary = "分页查询商品评价")
    @GetMapping("/list")
    public CommonResult<IPage<ProductReview>> getReviewList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<ProductReview> pageParam = new Page<>(page, size);
        return CommonResult.success(productReviewService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询商品评价")
    @GetMapping("/{id}")
    public CommonResult<ProductReview> getReviewById(@PathVariable Long id) {
        ProductReview review = productReviewService.selectById(id);
        return review != null ? CommonResult.success(review) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增商品评价")
    @PostMapping("/add")
    public CommonResult<Boolean> addProductReview(@Valid @RequestBody ProductReview productReview) {
        boolean result = productReviewService.insertProductReview(productReview);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新商品评价")
    @PutMapping("/update")
    public CommonResult<Boolean> updateProductReview(@Valid @RequestBody ProductReview productReview) {
        boolean result = productReviewService.updateProductReview(productReview);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除商品评价")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteProductReview(@PathVariable Long id) {
        boolean result = productReviewService.deleteProductReview(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 