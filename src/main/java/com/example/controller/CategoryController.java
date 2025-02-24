package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.Category;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "商品分类的增删改查")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "根据分类名称查询分类")
    @GetMapping("/name/{name}")
    public CommonResult<List<Category>> getCategoriesByName(@PathVariable String name) {
        List<Category> categories = categoryService.selectByName(name);
        return CommonResult.success(categories);
    }

    @Operation(summary = "分页查询分类")
    @GetMapping("/list")
    public CommonResult<IPage<Category>> getCategoryList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Category> pageParam = new Page<>(page, size);
        return CommonResult.success(categoryService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询分类")
    @GetMapping("/{id}")
    public CommonResult<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.selectById(id);
        return category != null ? CommonResult.success(category) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增分类")
    @PostMapping("/add")
    public CommonResult<Boolean> addCategory(@Valid @RequestBody Category category) {
        boolean result = categoryService.insertCategory(category);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新分类")
    @PutMapping("/update")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody Category category) {
        boolean result = categoryService.updateCategory(category);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除分类")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteCategory(@PathVariable Long id) {
        boolean result = categoryService.deleteCategory(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 