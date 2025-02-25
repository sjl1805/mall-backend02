package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.ResultCode;
import com.example.common.Result;
import com.example.model.entity.Category;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品分类控制器
 * <p>
 * 提供商品分类相关的API接口，包括分类的查询、创建、修改、删除等操作
 * 商品分类是电商系统中重要的基础结构，用于组织和展示商品
 */
@Tag(name = "Category", description = "商品分类管理API")
@RestController
@RequestMapping("/category")
@Validated
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "根据分类名称查询分类", description = "模糊匹配分类名称进行搜索")
    @GetMapping("/name/{name}")
    public  Result<List<Category>> getCategoriesByName(
            @Parameter(description = "分类名称", required = true) @PathVariable String name) {
        log.info("根据分类名称查询分类请求: name={}", name);
        List<Category> categories = categoryService.selectByName(name);
        log.info("根据分类名称查询分类成功: name={}, count={}", name, categories.size());
        return  Result.success(categories);
    }

    @Operation(summary = "分页查询分类", description = "分页查询所有分类")
    @GetMapping("/list")
    public  Result<IPage<Category>> getCategoryList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询分类请求: page={}, size={}", page, size);
        IPage<Category> pageParam = new Page<>(page, size);
        IPage<Category> result = categoryService.selectPage(pageParam);
        log.info("分页查询分类成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }

    @Operation(summary = "根据ID查询分类", description = "获取特定分类的详细信息")
    @GetMapping("/{id}")
    public  Result<Category> getCategoryById(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id) {
        log.info("根据ID查询分类请求: id={}", id);
        Category category = categoryService.selectById(id);
        if (category != null) {
            log.info("根据ID查询分类成功: id={}", id);
            return  Result.success(category);
        } else {
            log.warn("根据ID查询分类失败: id={}, 分类不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "分类不存在");
        }
    }

    @Operation(summary = "新增分类", description = "创建新的商品分类")
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> addCategory(@Valid @RequestBody Category category) {
        log.info("新增分类请求: name={}, level={}, parentId={}", 
                category.getName(), category.getLevel(), category.getParentId());
        
        // 参数验证
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            log.warn("新增分类失败: 名称为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "分类名称不能为空");
        }
        
        if (category.getLevel() != null && (category.getLevel() < 1 || category.getLevel() > 3)) {
            log.warn("新增分类失败: 层级无效, level={}", category.getLevel());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "分类层级必须在1-3之间");
        }
        
        boolean result = categoryService.insertCategory(category);
        if (result) {
            log.info("新增分类成功: name={}, id={}", category.getName(), category.getId());
            return  Result.success(true);
        } else {
            log.warn("新增分类失败: name={}", category.getName());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新分类", description = "更新现有分类信息")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> updateCategory(@Valid @RequestBody Category category) {
        log.info("更新分类请求: id={}", category.getId());
        
        // 参数验证
        if (category.getId() == null) {
            log.warn("更新分类失败: ID不能为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "分类ID不能为空");
        }
        
        if (category.getName() != null && category.getName().trim().isEmpty()) {
            log.warn("更新分类失败: 名称为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "分类名称不能为空");
        }
        
        boolean result = categoryService.updateCategory(category);
        if (result) {
            log.info("更新分类成功: id={}", category.getId());
            return  Result.success(true);
        } else {
            log.warn("更新分类失败: id={}", category.getId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除分类", description = "删除指定的分类")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> deleteCategory(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id) {
        log.info("删除分类请求: id={}", id);
        
        // 检查分类是否存在
        Category category = categoryService.selectById(id);
        if (category == null) {
            log.warn("删除分类失败: id={}, 分类不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "分类不存在");
        }
        
        // 检查是否可以安全删除
        if (!categoryService.checkCanDelete(id)) {
            log.warn("删除分类失败: id={}, 分类下有子分类或关联商品", id);
            return  Result.failed(ResultCode.FAILED, "无法删除，该分类下有子分类或关联商品");
        }
        
        boolean result = categoryService.deleteCategory(id);
        if (result) {
            log.info("删除分类成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除分类失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "根据父ID查询子分类", description = "获取指定父分类下的所有子分类")
    @GetMapping("/children/{parentId}")
    public  Result<List<Category>> getChildrenByParentId(
            @Parameter(description = "父分类ID，0表示顶级分类", required = true) 
            @PathVariable Long parentId) {
        log.info("根据父ID查询子分类请求: parentId={}", parentId);
        List<Category> children = categoryService.selectByParentId(parentId);
        log.info("根据父ID查询子分类成功: parentId={}, count={}", parentId, children.size());
        return  Result.success(children);
    }
    
    @Operation(summary = "根据分类层级查询分类", description = "获取特定层级的所有分类")
    @GetMapping("/level/{level}")
    public  Result<List<Category>> getCategoriesByLevel(
            @Parameter(description = "分类层级（1-一级 2-二级 3-三级）", required = true) 
            @PathVariable Integer level) {
        log.info("根据分类层级查询分类请求: level={}", level);
        
        // 参数验证
        if (level < 1 || level > 3) {
            log.warn("根据分类层级查询分类失败: 层级无效, level={}", level);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "分类层级必须在1-3之间");
        }
        
        List<Category> categories = categoryService.selectByLevel(level);
        log.info("根据分类层级查询分类成功: level={}, count={}", level, categories.size());
        return  Result.success(categories);
    }
    
    @Operation(summary = "获取分类树结构", description = "获取所有分类的树状结构")
    @GetMapping("/tree")
    public  Result<List<Map<String, Object>>> getCategoryTree() {
        log.info("获取分类树结构请求");
        List<Map<String, Object>> tree = categoryService.selectCategoryTree();
        log.info("获取分类树结构成功: rootCount={}", tree.size());
        return  Result.success(tree);
    }
    
    @Operation(summary = "获取子分类及其商品数量", description = "获取指定父分类下的子分类及每个子分类的商品数量")
    @GetMapping("/children/count/{parentId}")
    public  Result<List<Map<String, Object>>> getChildrenWithProductCount(
            @Parameter(description = "父分类ID", required = true) @PathVariable Long parentId) {
        log.info("获取子分类及其商品数量请求: parentId={}", parentId);
        List<Map<String, Object>> result = categoryService.selectChildrenWithProductCount(parentId);
        log.info("获取子分类及其商品数量成功: parentId={}, count={}", parentId, result.size());
        return  Result.success(result);
    }
    
    @Operation(summary = "更新分类排序", description = "修改分类的排序值")
    @PutMapping("/{id}/sort/{sort}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> updateCategorySort(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id,
            @Parameter(description = "排序值，值越小越靠前", required = true) @PathVariable Integer sort) {
        log.info("更新分类排序请求: id={}, sort={}", id, sort);
        
        // 检查分类是否存在
        Category category = categoryService.selectById(id);
        if (category == null) {
            log.warn("更新分类排序失败: id={}, 分类不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "分类不存在");
        }
        
        boolean result = categoryService.updateSort(id, sort);
        if (result) {
            log.info("更新分类排序成功: id={}, sort={}", id, sort);
            return  Result.success(true);
        } else {
            log.warn("更新分类排序失败: id={}, sort={}", id, sort);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "更新分类状态", description = "启用或禁用分类")
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> updateCategoryStatus(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id,
            @Parameter(description = "状态: 0-禁用 1-启用", required = true) @PathVariable Integer status) {
        log.info("更新分类状态请求: id={}, status={}", id, status);
        
        // 检查分类是否存在
        Category category = categoryService.selectById(id);
        if (category == null) {
            log.warn("更新分类状态失败: id={}, 分类不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "分类不存在");
        }
        
        // 参数验证
        if (status != 0 && status != 1) {
            log.warn("更新分类状态失败: 状态无效, status={}", status);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "状态必须为0或1");
        }
        
        boolean result = categoryService.updateStatus(id, status);
        if (result) {
            log.info("更新分类状态成功: id={}, status={}", id, status);
            return  Result.success(true);
        } else {
            log.warn("更新分类状态失败: id={}, status={}", id, status);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "获取分类路径", description = "获取指定分类的路径（面包屑导航）")
    @GetMapping("/path/{categoryId}")
    public  Result<List<Category>> getCategoryPath(
            @Parameter(description = "分类ID", required = true) @PathVariable Long categoryId) {
        log.info("获取分类路径请求: categoryId={}", categoryId);
        List<Category> path = categoryService.selectCategoryPath(categoryId);
        log.info("获取分类路径成功: categoryId={}, pathDepth={}", categoryId, path.size());
        return  Result.success(path);
    }
    
    @Operation(summary = "批量删除分类", description = "批量删除多个分类")
    @DeleteMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> batchDeleteCategories(@RequestBody List<Long> ids) {
        log.info("批量删除分类请求: ids={}", ids);
        
        // 参数验证
        if (ids == null || ids.isEmpty()) {
            log.warn("批量删除分类失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "分类ID列表不能为空");
        }
        
        // 检查是否所有分类都可以安全删除
        for (Long id : ids) {
            if (!categoryService.checkCanDelete(id)) {
                log.warn("批量删除分类失败: id={}, 分类下有子分类或关联商品", id);
                return  Result.failed(ResultCode.FAILED, "无法删除ID为" + id + "的分类，该分类下有子分类或关联商品");
            }
        }
        
        boolean result = categoryService.batchDeleteCategories(ids);
        if (result) {
            log.info("批量删除分类成功: count={}", ids.size());
            return  Result.success(true);
        } else {
            log.warn("批量删除分类失败: ids={}", ids);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "批量更新分类状态", description = "批量启用或禁用多个分类")
    @PutMapping("/batch/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> batchUpdateStatus(
            @RequestBody List<Long> ids,
            @Parameter(description = "状态: 0-禁用 1-启用", required = true) 
            @PathVariable Integer status) {
        log.info("批量更新分类状态请求: ids={}, status={}", ids, status);
        
        // 参数验证
        if (ids == null || ids.isEmpty()) {
            log.warn("批量更新分类状态失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "分类ID列表不能为空");
        }
        
        if (status != 0 && status != 1) {
            log.warn("批量更新分类状态失败: 状态无效, status={}", status);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "状态必须为0或1");
        }
        
        boolean result = categoryService.batchUpdateStatus(ids, status);
        if (result) {
            log.info("批量更新分类状态成功: ids={}, status={}", ids, status);
            return  Result.success(true);
        } else {
            log.warn("批量更新分类状态失败: ids={}, status={}", ids, status);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "检查分类是否可以安全删除", description = "检查分类是否有子分类和关联的商品")
    @GetMapping("/check-delete/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Boolean> checkCategoryCanDelete(
            @Parameter(description = "分类ID", required = true) @PathVariable Long categoryId) {
        log.info("检查分类是否可以安全删除请求: categoryId={}", categoryId);
        boolean canDelete = categoryService.checkCanDelete(categoryId);
        log.info("检查分类是否可以安全删除结果: categoryId={}, canDelete={}", categoryId, canDelete);
        return  Result.success(canDelete);
    }
    
    @Operation(summary = "批量导入分类数据", description = "批量导入分类数据，通常用于系统初始化或数据迁移")
    @PostMapping("/batch/import")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Integer> batchImportCategories(@Valid @RequestBody List<Category> categories) {
        log.info("批量导入分类数据请求: count={}", categories.size());
        
        if (categories.isEmpty()) {
            log.warn("批量导入分类数据失败: 分类列表为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "分类列表不能为空");
        }
        
        int count = categoryService.batchImport(categories);
        log.info("批量导入分类数据成功: successCount={}", count);
        return  Result.success(count);
    }
    
    @Operation(summary = "导出分类数据", description = "导出分类数据，支持按层级筛选")
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<List<Category>> exportCategories(
            @Parameter(description = "分类层级（可选）") @RequestParam(required = false) Integer level) {
        log.info("导出分类数据请求: level={}", level);
        
        if (level != null && (level < 1 || level > 3)) {
            log.warn("导出分类数据失败: 层级无效, level={}", level);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "分类层级必须在1-3之间");
        }
        
        List<Category> categories = categoryService.exportCategories(level);
        log.info("导出分类数据成功: count={}", categories.size());
        return  Result.success(categories);
    }
    
    @Operation(summary = "查询热门分类", description = "获取商品数量最多的分类")
    @GetMapping("/hot")
    public  Result<List<Map<String, Object>>> getHotCategories(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("查询热门分类请求: limit={}", limit);
        
        if (limit <= 0) {
            log.warn("查询热门分类失败: 限制数量无效, limit={}", limit);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "限制数量必须大于0");
        }
        
        List<Map<String, Object>> hotCategories = categoryService.getHotCategories(limit);
        log.info("查询热门分类成功: count={}", hotCategories.size());
        return  Result.success(hotCategories);
    }
    
    @Operation(summary = "统计每个层级的分类数量", description = "统计每个层级的分类数量，用于数据分析和监控")
    @GetMapping("/count-by-level")
    @PreAuthorize("hasRole('ADMIN')")
    public  Result<Map<Integer, Integer>> countCategoriesByLevel() {
        log.info("统计每个层级的分类数量请求");
        Map<Integer, Integer> countMap = categoryService.countCategoriesByLevel();
        log.info("统计每个层级的分类数量成功: countMap={}", countMap);
        return  Result.success(countMap);
    }
    
    @Operation(summary = "根据关键词搜索分类", description = "根据关键词搜索匹配的分类，支持模糊匹配")
    @GetMapping("/search")
    public  Result<List<Category>> searchCategories(
            @Parameter(description = "搜索关键词", required = true) @RequestParam String keyword) {
        log.info("根据关键词搜索分类请求: keyword={}", keyword);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("根据关键词搜索分类失败: 关键词为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "搜索关键词不能为空");
        }
        
        List<Category> categories = categoryService.searchCategories(keyword);
        log.info("根据关键词搜索分类成功: keyword={}, count={}", keyword, categories.size());
        return  Result.success(categories);
    }
    
    @Operation(summary = "获取完整的分类树", description = "获取完整的分类树结构，包含所有层级和商品数量")
    @GetMapping("/full-tree")
    public  Result<List<Category>> getFullCategoryTree() {
        log.info("获取完整的分类树请求");
        List<Category> tree = categoryService.getFullCategoryTree();
        log.info("获取完整的分类树成功: rootCount={}", tree.size());
        return  Result.success(tree);
    }
} 

