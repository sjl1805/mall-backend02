package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.category.CategoryPageDTO;
import com.example.model.dto.category.CategoryDTO;
import com.example.model.entity.Category;
import com.example.service.CategoryService;
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

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "商品分类管理接口")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "分页查询分类", description = "获取带分页的分类列表")
    @ApiResponse(responseCode = "200", description = "成功返回分页数据")
    public Result<IPage<Category>> listCategories(
            @Parameter(description = "分页查询参数") @Valid CategoryPageDTO queryDTO) {
        return Result.success(categoryService.listCategoryPage(queryDTO));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建分类", description = "添加新的商品分类")
    @ApiResponse(responseCode = "201", description = "分类创建成功")
    public Result<Boolean> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "分类数据",
                required = true,
                content = @Content(schema = @Schema(implementation = CategoryDTO.class))
            )
            @Valid @RequestBody CategoryDTO categoryDTO) {
        return Result.success(categoryService.addCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "修改指定分类信息")
    @ApiResponse(responseCode = "200", description = "分类更新成功")
    public Result<Boolean> updateCategory(
            @Parameter(description = "分类ID", example = "1") @PathVariable @Min(1) Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setId(id);
        return Result.success(categoryService.updateCategory(categoryDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "删除指定分类及其子分类")
    @ApiResponse(responseCode = "200", description = "分类删除成功")
    public Result<Boolean> deleteCategory(
            @Parameter(description = "分类ID", example = "1") @PathVariable @Min(1) Long id) {
        return Result.success(categoryService.deleteCategory(id));
    }

    @GetMapping("/tree")
    @Operation(summary = "获取分类树", description = "获取全部分类的树形结构")
    @ApiResponse(responseCode = "200", description = "成功返回树形结构")
    public Result<List<Category>> getCategoryTree() {
        return Result.success(categoryService.getCategoryTree());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "切换分类状态", description = "启用/禁用分类及其子分类")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    public Result<Boolean> switchStatus(
            @Parameter(description = "分类ID", example = "1") @PathVariable @Min(1) Long id,
            @Parameter(description = "新状态 0-禁用 1-启用", example = "1") 
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(categoryService.switchStatus(id, status));
    }
} 