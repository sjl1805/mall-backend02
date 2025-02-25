package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common. Result;
import com.example.common.ResultCode;
import com.example.model.entity.Products;
import com.example.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品控制器
 * <p>
 * 提供商品相关的API接口，包括商品的查询、添加、修改、删除等操作
 */
@Tag(name = "Products", description = "商品管理API")
@RestController
@RequestMapping("/products")
@Validated
@Slf4j
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @Operation(summary = "根据商品名称查询商品", description = "模糊匹配商品名称进行搜索")
    @GetMapping("/name/{name}")
    public  Result<List<Products>> getProductsByName(
            @Parameter(description = "商品名称", required = true) @PathVariable String name) {
        log.info("根据商品名称查询商品请求: name={}", name);
        List<Products> products = productsService.selectByName(name);
        log.info("根据商品名称查询商品成功: name={}, count={}", name, products.size());
        return  Result.success(products);
    }

    @Operation(summary = "分页查询商品", description = "获取商品列表，支持分页")
    @GetMapping("/list")
    public  Result<IPage<Products>> getProductList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询商品请求: page={}, size={}", page, size);
        IPage<Products> pageParam = new Page<>(page, size);
        IPage<Products> result = productsService.selectPage(pageParam);
        log.info("分页查询商品成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }

    @Operation(summary = "根据ID查询商品", description = "获取商品详细信息")
    @GetMapping("/{id}")
    public  Result<Products> getProductById(
            @Parameter(description = "商品ID", required = true) @PathVariable Long id) {
        log.info("根据ID查询商品请求: id={}", id);
        Products product = productsService.selectById(id);
        if (product != null) {
            log.info("根据ID查询商品成功: id={}", id);
            return  Result.success(product);
        } else {
            log.warn("根据ID查询商品失败: id={}, 商品不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "商品不存在");
        }
    }

    @Operation(summary = "新增商品", description = "添加新的商品")
    @PostMapping("/add")
    public  Result<Boolean> addProduct(@Valid @RequestBody Products product) {
        log.info("新增商品请求: name={}, categoryId={}", product.getName(), product.getCategoryId());
        boolean result = productsService.insertProduct(product);
        if (result) {
            log.info("新增商品成功: name={}, id={}", product.getName(), product.getId());
            return  Result.success(true);
        } else {
            log.warn("新增商品失败: name={}", product.getName());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新商品", description = "更新现有商品信息")
    @PutMapping("/update")
    public  Result<Boolean> updateProduct(@Valid @RequestBody Products product) {
        log.info("更新商品请求: id={}", product.getId());
        boolean result = productsService.updateProduct(product);
        if (result) {
            log.info("更新商品成功: id={}", product.getId());
            return  Result.success(true);
        } else {
            log.warn("更新商品失败: id={}", product.getId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除商品", description = "删除指定的商品")
    @DeleteMapping("/{id}")
    public  Result<Boolean> deleteProduct(
            @Parameter(description = "商品ID", required = true) @PathVariable Long id) {
        log.info("删除商品请求: id={}", id);
        boolean result = productsService.deleteProduct(id);
        if (result) {
            log.info("删除商品成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除商品失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "根据分类ID查询商品", description = "获取指定分类的商品列表")
    @GetMapping("/category/{categoryId}")
    public  Result<IPage<Products>> getProductsByCategoryId(
            @Parameter(description = "分类ID", required = true) @PathVariable Long categoryId,
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("根据分类ID查询商品请求: categoryId={}, page={}, size={}", categoryId, page, size);
        IPage<Products> pageParam = new Page<>(page, size);
        IPage<Products> result = productsService.selectByCategoryId(categoryId, pageParam);
        log.info("根据分类ID查询商品成功: categoryId={}, totalRecords={}", categoryId, result.getTotal());
        return  Result.success(result);
    }
    
    @Operation(summary = "获取热门商品", description = "获取销量最高的商品")
    @GetMapping("/hot")
    public  Result<List<Products>> getHotProducts(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取热门商品请求: limit={}", limit);
        List<Products> products = productsService.getHotProducts(limit);
        log.info("获取热门商品成功: count={}", products.size());
        return  Result.success(products);
    }
    
    @Operation(summary = "获取推荐商品", description = "获取系统推荐的商品")
    @GetMapping("/recommend")
    public  Result<List<Products>> getRecommendProducts(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取推荐商品请求: limit={}", limit);
        List<Products> products = productsService.getRecommendProducts(limit);
        log.info("获取推荐商品成功: count={}", products.size());
        return  Result.success(products);
    }
    
    @Operation(summary = "获取新品", description = "获取最近添加的商品")
    @GetMapping("/new")
    public  Result<List<Products>> getNewProducts(
            @Parameter(description = "最近天数") @RequestParam(defaultValue = "7") Integer days,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取新品请求: days={}, limit={}", days, limit);
        List<Products> products = productsService.getNewProducts(days, limit);
        log.info("获取新品成功: count={}", products.size());
        return  Result.success(products);
    }
    
    @Operation(summary = "更新商品库存", description = "增加或减少商品库存")
    @PutMapping("/{id}/stock/{quantity}")
    public  Result<Boolean> updateStock(
            @Parameter(description = "商品ID", required = true) @PathVariable Long id,
            @Parameter(description = "变动数量（正数增加，负数减少）", required = true) @PathVariable Integer quantity) {
        log.info("更新商品库存请求: id={}, quantity={}", id, quantity);
        boolean result = productsService.updateStock(id, quantity);
        if (result) {
            log.info("更新商品库存成功: id={}, quantity={}", id, quantity);
            return  Result.success(true);
        } else {
            log.warn("更新商品库存失败: id={}, quantity={}", id, quantity);
            return  Result.failed(ResultCode.FAILED, "库存更新失败，请检查库存是否充足");
        }
    }
    
    @Operation(summary = "批量更新商品状态", description = "批量更新商品的上架、下架状态")
    @PutMapping("/batch/status/{status}")
    public  Result<Boolean> batchUpdateStatus(
            @Parameter(description = "商品ID列表", required = true) @RequestBody List<Long> ids,
            @Parameter(description = "状态值: 0-下架 1-上架", required = true) @PathVariable Integer status) {
        log.info("批量更新商品状态请求: ids={}, status={}", ids, status);
        
        // 验证参数有效性
        if (ids == null || ids.isEmpty()) {
            log.warn("批量更新商品状态失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "商品ID列表不能为空");
        }
        
        if (status != 0 && status != 1) {
            log.warn("批量更新商品状态失败: 状态值无效, status={}", status);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "状态值无效");
        }
        
        boolean result = productsService.batchUpdateStatus(ids, status);
        if (result) {
            log.info("批量更新商品状态成功: ids={}, status={}", ids, status);
            return  Result.success(true);
        } else {
            log.warn("批量更新商品状态失败: ids={}, status={}", ids, status);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "统计商品销量排行", description = "统计指定时间段内的商品销量排行")
    @GetMapping("/sales/ranking")
    public  Result<List<Map<String, Object>>> getProductSalesRanking(
            @Parameter(description = "开始日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Parameter(description = "结束日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("统计商品销量排行请求: startDate={}, endDate={}, limit={}", startDate, endDate, limit);
        
        // 验证时间范围有效性
        if (startDate.after(endDate)) {
            log.warn("统计商品销量排行失败: 时间范围无效, startDate={}, endDate={}", startDate, endDate);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "开始日期不能晚于结束日期");
        }
        
        List<Map<String, Object>> ranking = productsService.getProductSalesRanking(startDate, endDate, limit);
        log.info("统计商品销量排行成功: count={}", ranking.size());
        return  Result.success(ranking);
    }
    
    @Operation(summary = "获取完整商品详情", description = "获取包含分类、评价等完整信息的商品详情")
    @GetMapping("/detail/{id}")
    public  Result<Products> getProductDetail(
            @Parameter(description = "商品ID", required = true) @PathVariable Long id) {
        log.info("获取完整商品详情请求: id={}", id);
        Products product = productsService.getProductDetail(id);
        if (product != null) {
            log.info("获取完整商品详情成功: id={}", id);
            return  Result.success(product);
        } else {
            log.warn("获取完整商品详情失败: id={}, 商品不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "商品不存在");
        }
    }
    
    @Operation(summary = "批量导入商品", description = "批量导入商品数据")
    @PostMapping("/batch/import")
    public  Result<Integer> batchImportProducts(@Valid @RequestBody List<Products> productList) {
        log.info("批量导入商品请求: count={}", productList.size());
        
        if (productList.isEmpty()) {
            log.warn("批量导入商品失败: 商品列表为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "商品列表不能为空");
        }
        
        int count = productsService.batchImportProducts(productList);
        log.info("批量导入商品成功: successCount={}", count);
        return  Result.success(count);
    }
    
    @Operation(summary = "导出商品数据", description = "导出符合条件的商品数据")
    @GetMapping("/export")
    public  Result<List<Products>> exportProducts(
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        log.info("导出商品数据请求: categoryId={}, keyword={}", categoryId, keyword);
        List<Products> products = productsService.exportProducts(categoryId, keyword);
        log.info("导出商品数据成功: count={}", products.size());
        return  Result.success(products);
    }
} 

