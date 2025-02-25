package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.CommonResult;
import com.example.common.ResultCode;
import com.example.model.entity.RecommendProduct;
import com.example.service.RecommendProductService;
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

/**
 * 推荐商品控制器
 * <p>
 * 提供商品推荐相关的API接口，包括个性化推荐、相似商品推荐和管理功能
 */
@Tag(name = "RecommendProduct", description = "商品推荐系统API")
@RestController
@RequestMapping("/recommendProduct")
@Validated
@Slf4j
public class RecommendProductController {

    @Autowired
    private RecommendProductService recommendProductService;

    @Operation(summary = "根据用户ID查询推荐商品", description = "获取特定用户的个性化推荐商品")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public CommonResult<List<RecommendProduct>> getProductsByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取用户推荐商品请求: userId={}", userId);
        List<RecommendProduct> products = recommendProductService.selectByUserId(userId);
        log.info("获取用户推荐商品成功: userId={}, count={}", userId, products.size());
        return CommonResult.success(products);
    }

    @Operation(summary = "分页查询推荐商品", description = "管理员分页查询所有推荐商品")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<IPage<RecommendProduct>> getRecommendProductList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询推荐商品请求: page={}, size={}", page, size);
        IPage<RecommendProduct> pageParam = new Page<>(page, size);
        IPage<RecommendProduct> result = recommendProductService.selectPage(pageParam);
        log.info("分页查询推荐商品成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return CommonResult.success(result);
    }

    @Operation(summary = "根据ID查询推荐商品", description = "获取推荐商品详情")
    @GetMapping("/{id}")
    public CommonResult<RecommendProduct> getRecommendProductById(
            @Parameter(description = "推荐商品ID", required = true) @PathVariable Long id) {
        log.info("查询推荐商品详情请求: id={}", id);
        RecommendProduct product = recommendProductService.selectById(id);
        if (product != null) {
            log.info("查询推荐商品详情成功: id={}", id);
            return CommonResult.success(product);
        } else {
            log.warn("查询推荐商品详情失败: id={}, 推荐商品不存在", id);
            return CommonResult.failed(ResultCode.NOT_FOUND, "推荐商品不存在");
        }
    }

    @Operation(summary = "新增推荐商品", description = "添加新的推荐商品")
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> addRecommendProduct(@Valid @RequestBody RecommendProduct recommendProduct) {
        log.info("新增推荐商品请求: productId={}, type={}", recommendProduct.getProductId(), recommendProduct.getType());
        boolean result = recommendProductService.insertRecommendProduct(recommendProduct);
        if (result) {
            log.info("新增推荐商品成功: productId={}, type={}", recommendProduct.getProductId(), recommendProduct.getType());
            return CommonResult.success(true);
        } else {
            log.warn("新增推荐商品失败: productId={}, type={}", recommendProduct.getProductId(), recommendProduct.getType());
            return CommonResult.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新推荐商品", description = "更新现有推荐商品信息")
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> updateRecommendProduct(@Valid @RequestBody RecommendProduct recommendProduct) {
        log.info("更新推荐商品请求: id={}", recommendProduct.getId());
        boolean result = recommendProductService.updateRecommendProduct(recommendProduct);
        if (result) {
            log.info("更新推荐商品成功: id={}", recommendProduct.getId());
            return CommonResult.success(true);
        } else {
            log.warn("更新推荐商品失败: id={}", recommendProduct.getId());
            return CommonResult.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除推荐商品", description = "删除指定的推荐商品")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> deleteRecommendProduct(
            @Parameter(description = "推荐商品ID", required = true) @PathVariable Long id) {
        log.info("删除推荐商品请求: id={}", id);
        boolean result = recommendProductService.deleteRecommendProduct(id);
        if (result) {
            log.info("删除推荐商品成功: id={}", id);
            return CommonResult.success(true);
        } else {
            log.warn("删除推荐商品失败: id={}", id);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "根据商品ID查询推荐商品", description = "获取与特定商品相关的推荐")
    @GetMapping("/product/{productId}")
    public CommonResult<List<RecommendProduct>> getRecommendsByProductId(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId) {
        log.info("根据商品ID查询推荐商品请求: productId={}", productId);
        List<RecommendProduct> recommends = recommendProductService.selectByProductId(productId);
        log.info("根据商品ID查询推荐商品成功: productId={}, count={}", productId, recommends.size());
        return CommonResult.success(recommends);
    }
    
    @Operation(summary = "查询活跃推荐商品", description = "获取当前有效的推荐商品")
    @GetMapping("/active")
    public CommonResult<List<RecommendProduct>> getActiveRecommends(
            @Parameter(description = "推荐类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("查询活跃推荐商品请求: type={}, limit={}", type, limit);
        List<RecommendProduct> recommends = recommendProductService.selectActiveRecommends(type, limit);
        log.info("查询活跃推荐商品成功: type={}, limit={}, count={}", type, limit, recommends.size());
        return CommonResult.success(recommends);
    }
    
    @Operation(summary = "按类型查询推荐商品", description = "获取特定类型的推荐商品")
    @GetMapping("/type/{type}")
    public CommonResult<List<RecommendProduct>> getRecommendsByType(
            @Parameter(description = "推荐类型", required = true) @PathVariable Integer type) {
        log.info("按类型查询推荐商品请求: type={}", type);
        List<RecommendProduct> recommends = recommendProductService.selectByType(type);
        log.info("按类型查询推荐商品成功: type={}, count={}", type, recommends.size());
        return CommonResult.success(recommends);
    }
    
    @Operation(summary = "更新推荐商品状态", description = "启用或禁用推荐商品")
    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> updateRecommendStatus(
            @Parameter(description = "推荐商品ID", required = true) @PathVariable Long id,
            @Parameter(description = "新状态: 0-禁用 1-启用", required = true) @PathVariable Integer status) {
        log.info("更新推荐商品状态请求: id={}, status={}", id, status);
        
        // 验证状态参数
        if (status != 0 && status != 1) {
            log.warn("更新推荐商品状态失败: 状态值无效, status={}", status);
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "状态值无效");
        }
        
        boolean result = recommendProductService.updateStatus(id, status);
        if (result) {
            log.info("更新推荐商品状态成功: id={}, status={}", id, status);
            return CommonResult.success(true);
        } else {
            log.warn("更新推荐商品状态失败: id={}, status={}", id, status);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "生成个性化推荐", description = "基于用户行为生成个性化商品推荐")
    @GetMapping("/personalized/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public CommonResult<List<RecommendProduct>> generatePersonalizedRecommends(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "推荐数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("生成个性化推荐请求: userId={}, limit={}", userId, limit);
        List<RecommendProduct> recommends = recommendProductService.generatePersonalizedRecommends(userId, limit);
        log.info("生成个性化推荐成功: userId={}, limit={}, count={}", userId, limit, recommends.size());
        return CommonResult.success(recommends);
    }
    
    @Operation(summary = "生成相似商品推荐", description = "基于当前商品特性推荐相似商品")
    @GetMapping("/similar/{productId}")
    public CommonResult<List<RecommendProduct>> generateSimilarProductRecommends(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId,
            @Parameter(description = "推荐数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("生成相似商品推荐请求: productId={}, limit={}", productId, limit);
        List<RecommendProduct> recommends = recommendProductService.generateSimilarProductRecommends(productId, limit);
        log.info("生成相似商品推荐成功: productId={}, limit={}, count={}", productId, limit, recommends.size());
        return CommonResult.success(recommends);
    }
    
    @Operation(summary = "生成猜你喜欢推荐", description = "根据用户历史行为生成猜你喜欢推荐")
    @GetMapping("/youmaylike/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public CommonResult<List<RecommendProduct>> generateYouMayLikeRecommends(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId,
            @Parameter(description = "推荐数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("生成猜你喜欢推荐请求: userId={}, limit={}", userId, limit);
        List<RecommendProduct> recommends = recommendProductService.generateYouMayLikeRecommends(userId, limit);
        log.info("生成猜你喜欢推荐成功: userId={}, limit={}, count={}", userId, limit, recommends.size());
        return CommonResult.success(recommends);
    }
    
    @Operation(summary = "生成购买此商品的人还购买了推荐", description = "基于协同过滤算法的相关商品推荐")
    @GetMapping("/alsobought/{productId}")
    public CommonResult<List<RecommendProduct>> generateAlsoBoughtRecommends(
            @Parameter(description = "商品ID", required = true) @PathVariable Long productId,
            @Parameter(description = "推荐数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("生成'购买此商品的人还购买了'推荐请求: productId={}, limit={}", productId, limit);
        List<RecommendProduct> recommends = recommendProductService.generateAlsoBoughtRecommends(productId, limit);
        log.info("生成'购买此商品的人还购买了'推荐成功: productId={}, limit={}, count={}", productId, limit, recommends.size());
        return CommonResult.success(recommends);
    }
    
    @Operation(summary = "批量更新推荐商品状态", description = "批量启用或禁用推荐商品")
    @PutMapping("/batch/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Boolean> batchUpdateStatus(
            @RequestBody List<Long> ids,
            @Parameter(description = "新状态: 0-禁用 1-启用", required = true) @PathVariable Integer status) {
        log.info("批量更新推荐商品状态请求: ids={}, status={}", ids, status);
        
        // 验证参数有效性
        if (ids == null || ids.isEmpty()) {
            log.warn("批量更新推荐商品状态失败: 参数无效, ids为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "ID列表不能为空");
        }
        
        if (status != 0 && status != 1) {
            log.warn("批量更新推荐商品状态失败: 状态值无效, status={}", status);
            return CommonResult.failed(ResultCode.VALIDATE_FAILED, "状态值无效");
        }
        
        boolean result = recommendProductService.batchUpdateStatus(ids, status);
        if (result) {
            log.info("批量更新推荐商品状态成功: ids={}, status={}", ids, status);
            return CommonResult.success(true);
        } else {
            log.warn("批量更新推荐商品状态失败: ids={}, status={}", ids, status);
            return CommonResult.failed(ResultCode.FAILED);
        }
    }
} 