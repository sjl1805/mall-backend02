package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.CategoryDTO;
import com.example.model.dto.PageDTO;
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

/**
 * 商品分类管理控制器
 * 
 * @author 31815
 * @description 提供商品分类全生命周期管理功能，包含：
 *              1. 分类层级管理
 *              2. 分类状态控制
 *              3. 分类树形结构维护
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "商品分类管理接口体系")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 分页查询分类（带缓存）
     * @param queryDTO 分页参数：
     *                 - page: 当前页码（默认1）
     *                 - size: 每页数量（默认10，最大50）
     * @return 分页结果包装对象
     * @implNote 结果缓存优化，有效期5分钟
     */
    @GetMapping
    @Operation(summary = "分类分页查询", description = "获取带分页的分类列表接口")
    @ApiResponse(responseCode = "200", description = "成功返回分页数据")
    public Result<IPage<CategoryDTO>> listCategories(
            @Parameter(description = "分页查询参数") @Valid PageDTO<CategoryDTO> queryDTO) {
        return Result.success(categoryService.listCategoryPage(queryDTO));
    }

    /**
     * 创建分类（事务操作）
     * @param categoryDTO 分类信息：
     *                   - categoryName: 分类名称（必须，最多20字）
     *                   - parentId: 父分类ID（必须，0表示根分类）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - CATEGORY_DUPLICATE(16001): 重复分类名称
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "分类创建", description = "添加新的商品分类接口")
    @ApiResponse(responseCode = "201", description = "分类创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复分类名称")
    public Result<Boolean> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "分类数据",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))
            )
            @Valid @RequestBody CategoryDTO categoryDTO) {
        return Result.success(categoryService.addCategory(categoryDTO));
    }

    /**
     * 更新分类信息（事务操作）
     * @param id 分类ID（路径参数，必须大于0）
     * @param categoryDTO 更新信息：
     *                   - categoryName: 新名称（可选）
     *                   - parentId: 新父分类ID（可选）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - CATEGORY_NOT_FOUND(16002): 分类不存在
     */
    @PutMapping("/{id}")
    @Operation(summary = "分类信息更新", description = "修改指定分类信息接口")
    @ApiResponse(responseCode = "200", description = "分类更新成功")
    @ApiResponse(responseCode = "404", description = "分类不存在")
    public Result<Boolean> updateCategory(
            @Parameter(description = "分类ID", example = "1", required = true)
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setId(id);
        return Result.success(categoryService.updateCategory(categoryDTO));
    }

    /**
     * 删除分类（事务操作）
     * @param id 分类ID（路径参数，必须大于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - CATEGORY_HAS_CHILDREN(16003): 存在子分类
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "分类删除", description = "删除指定分类及其子分类接口")
    @ApiResponse(responseCode = "200", description = "分类删除成功")
    @ApiResponse(responseCode = "400", description = "存在子分类")
    @ApiResponse(responseCode = "404", description = "分类不存在")
    public Result<Boolean> deleteCategory(
            @Parameter(description = "分类ID", example = "1", required = true)
            @PathVariable @Min(1) Long id) {
        return Result.success(categoryService.deleteCategory(id));
    }

    /**
     * 获取分类树（带缓存）
     * @return 全部分类的树形结构（按层级排序）
     * @implNote 结果缓存优化，有效期1小时
     */
    @GetMapping("/tree")
    @Operation(summary = "分类树查询", description = "获取全部分类的树形结构接口")
    @ApiResponse(responseCode = "200", description = "成功返回树形结构")
    public Result<List<CategoryDTO>> getCategoryTree(
            @Parameter(description = "父分类ID", example = "0", required = true)
            @RequestParam(required = false, defaultValue = "0") Long parentId) {
        return Result.success(categoryService.getCategoryTree(parentId));
    }

    /**
     * 切换分类状态（事务操作）
     * @param id 分类ID（路径参数，必须大于0）
     * @param status 新状态（0-禁用，1-启用）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - CATEGORY_NOT_FOUND(16002): 分类不存在
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "分类状态切换", description = "启用/禁用分类及其子分类接口")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @ApiResponse(responseCode = "404", description = "分类不存在")
    public Result<Boolean> switchStatus(
            @Parameter(description = "分类ID", example = "1", required = true)
            @PathVariable @Min(1) Long id,
            @Parameter(description = "新状态", schema = @Schema(allowableValues = {"0", "1"}), required = true)
            @RequestParam @NotNull @Min(0) @Max(1) Integer status) {
        return Result.success(categoryService.switchStatus(id, status));
    }
} 