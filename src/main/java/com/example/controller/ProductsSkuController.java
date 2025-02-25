package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common. Result;
import com.example.common.ResultCode;
import com.example.model.entity.ProductSku;
import com.example.service.ProductSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品SKU控制器
 * <p>
 * 提供商品SKU(库存单位)相关的API接口，包括SKU的查询、添加、修改、删除等操作
 * SKU是电商系统中最小的库存单位，代表了特定规格组合的商品实体
 */
@Tag(name = "ProductSku", description = "商品SKU管理API")
@RestController
@RequestMapping("/productSku")
@Validated
@Slf4j
public class ProductsSkuController {

    @Autowired
    private ProductSkuService productSkuService;

    @Operation(summary = "根据商品ID查询SKU", description = "获取指定商品的所有规格组合")
    @GetMapping("/product/{productId}")
    public  Result<List<ProductSku>> getSkusByProductId(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId) {
        log.info("根据商品ID查询SKU请求: productId={}", productId);
        List<ProductSku> skus = productSkuService.selectByProductId(productId);
        log.info("根据商品ID查询SKU成功: productId={}, count={}", productId, skus.size());
        return  Result.success(skus);
    }

    @Operation(summary = "分页查询SKU", description = "管理员分页查询所有SKU信息")
    @GetMapping("/list")
    public  Result<IPage<ProductSku>> getSkuList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询SKU请求: page={}, size={}", page, size);
        IPage<ProductSku> pageParam = new Page<>(page, size);
        IPage<ProductSku> result = productSkuService.selectPage(pageParam);
        log.info("分页查询SKU成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }

    @Operation(summary = "根据ID查询SKU", description = "获取特定SKU的详细信息")
    @GetMapping("/{id}")
    public  Result<ProductSku> getSkuById(
            @Parameter(description = "SKU ID", required = true) @PathVariable Long id) {
        log.info("根据ID查询SKU请求: id={}", id);
        ProductSku sku = productSkuService.selectById(id);
        if (sku != null) {
            log.info("根据ID查询SKU成功: id={}", id);
            return  Result.success(sku);
        } else {
            log.warn("根据ID查询SKU失败: id={}, SKU不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "SKU不存在");
        }
    }

    @Operation(summary = "新增SKU", description = "添加新的商品SKU")
    @PostMapping("/add")
    public  Result<Boolean> addProductSku(@Valid @RequestBody ProductSku productSku) {
        log.info("新增SKU请求: productId={}", productSku.getProductId());
        
        // 参数验证
        if (productSku.getPrice() == null || productSku.getPrice().doubleValue() < 0) {
            log.warn("新增SKU失败: 价格无效, price={}", productSku.getPrice());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "价格不能为空且必须大于等于0");
        }
        
        if (productSku.getStock() == null || productSku.getStock() < 0) {
            log.warn("新增SKU失败: 库存无效, stock={}", productSku.getStock());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "库存不能为空且必须大于等于0");
        }
        
        boolean result = productSkuService.insertProductSku(productSku);
        if (result) {
            log.info("新增SKU成功: productId={},  id={}", 
                    productSku.getProductId(), productSku.getId());
            return  Result.success(true);
        } else {
            log.warn("新增SKU失败: productId={}", 
                    productSku.getProductId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新SKU", description = "更新现有SKU信息")
    @PutMapping("/update")
    public  Result<Boolean> updateProductSku(@Valid @RequestBody ProductSku productSku) {
        log.info("更新SKU请求: id={}", productSku.getId());
        
        // 参数验证
        if (productSku.getId() == null) {
            log.warn("更新SKU失败: ID不能为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "SKU ID不能为空");
        }
        
        if (productSku.getPrice() != null && productSku.getPrice().doubleValue() < 0) {
            log.warn("更新SKU失败: 价格无效, price={}", productSku.getPrice());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "价格必须大于等于0");
        }
        
        if (productSku.getStock() != null && productSku.getStock() < 0) {
            log.warn("更新SKU失败: 库存无效, stock={}", productSku.getStock());
            return  Result.failed(ResultCode.VALIDATE_FAILED, "库存必须大于等于0");
        }
        
        boolean result = productSkuService.updateProductSku(productSku);
        if (result) {
            log.info("更新SKU成功: id={}", productSku.getId());
            return  Result.success(true);
        } else {
            log.warn("更新SKU失败: id={}", productSku.getId());
            return  Result.failed(ResultCode.FAILED, "SKU不存在或更新失败");
        }
    }

    @Operation(summary = "根据ID删除SKU", description = "删除指定的SKU")
    @DeleteMapping("/{id}")
    public  Result<Boolean> deleteProductSku(
            @Parameter(description = "SKU ID", required = true) @PathVariable Long id) {
        log.info("删除SKU请求: id={}", id);
        
        // 检查SKU是否存在
        ProductSku sku = productSkuService.selectById(id);
        if (sku == null) {
            log.warn("删除SKU失败: id={}, SKU不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "SKU不存在");
        }
        
        boolean result = productSkuService.deleteProductSku(id);
        if (result) {
            log.info("删除SKU成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除SKU失败: id={}", id);
            return  Result.failed(ResultCode.FAILED, "SKU删除失败，可能被订单或购物车引用");
        }
    }
    
    @Operation(summary = "批量添加SKU", description = "批量添加多个SKU记录")
    @PostMapping("/batch/add")
    public  Result<Boolean> batchAddProductSkus(@Valid @RequestBody List<ProductSku> skuList) {
        log.info("批量添加SKU请求: count={}", skuList.size());
        
        if (skuList.isEmpty()) {
            log.warn("批量添加SKU失败: SKU列表为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "SKU列表不能为空");
        }
        
        // 逐个添加SKU
        boolean allSuccess = true;
        for (ProductSku sku : skuList) {
            if (!productSkuService.insertProductSku(sku)) {
                allSuccess = false;
                log.warn("批量添加SKU部分失败: productId={}", sku.getProductId());
            }
        }
        
        if (allSuccess) {
            log.info("批量添加SKU成功: count={}", skuList.size());
            return  Result.success(true);
        } else {
            log.warn("批量添加SKU部分失败: totalCount={}", skuList.size());
            return  Result.failed(ResultCode.FAILED, "部分SKU添加失败");
        }
    }
    
    @Operation(summary = "更新SKU库存", description = "增加或减少SKU的库存")
    @PutMapping("/{id}/stock/{quantity}")
    public  Result<Boolean> updateSkuStock(
            @Parameter(description = "SKU ID", required = true) @PathVariable Long id,
            @Parameter(description = "变动数量（正数增加，负数减少）", required = true) @PathVariable Integer quantity) {
        log.info("更新SKU库存请求: id={}, quantity={}", id, quantity);
        
        // 检查SKU是否存在
        ProductSku sku = productSkuService.selectById(id);
        if (sku == null) {
            log.warn("更新SKU库存失败: id={}, SKU不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "SKU不存在");
        }
        
        // 检查减库存操作是否会导致库存不足
        if (quantity < 0 && sku.getStock() < Math.abs(quantity)) {
            log.warn("更新SKU库存失败: id={}, 库存不足, currentStock={}, quantity={}", id, sku.getStock(), quantity);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "库存不足");
        }
        
        // 更新库存
        sku.setStock(sku.getStock() + quantity);
        boolean result = productSkuService.updateProductSku(sku);
        
        if (result) {
            log.info("更新SKU库存成功: id={}, oldStock={}, newStock={}", id, sku.getStock() - quantity, sku.getStock());
            return  Result.success(true);
        } else {
            log.warn("更新SKU库存失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "批量删除SKU", description = "批量删除多个SKU记录")
    @DeleteMapping("/batch")
    public  Result<Boolean> batchDeleteProductSkus(@RequestBody List<Long> ids) {
        log.info("批量删除SKU请求: ids={}", ids);
        
        if (ids == null || ids.isEmpty()) {
            log.warn("批量删除SKU失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "SKU ID列表不能为空");
        }
        
        // 逐个删除SKU
        boolean allSuccess = true;
        for (Long id : ids) {
            if (!productSkuService.deleteProductSku(id)) {
                allSuccess = false;
                log.warn("批量删除SKU部分失败: id={}", id);
            }
        }
        
        if (allSuccess) {
            log.info("批量删除SKU成功: count={}", ids.size());
            return  Result.success(true);
        } else {
            log.warn("批量删除SKU部分失败: totalCount={}", ids.size());
            return  Result.failed(ResultCode.FAILED, "部分SKU删除失败，可能不存在或被引用");
        }
    }
} 

