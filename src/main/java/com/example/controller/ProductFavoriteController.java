package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common. Result;
import com.example.common.ResultCode;
import com.example.model.entity.ProductFavorite;
import com.example.service.ProductFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品收藏控制器
 * <p>
 * 提供商品收藏相关的API接口，包括收藏的查询、添加、删除等操作
 * 以及收藏夹管理、热门收藏等功能
 */
@Tag(name = "ProductFavorite", description = "商品收藏管理API")
@RestController
@RequestMapping("/productFavorite")
@Validated
@Slf4j
public class ProductFavoriteController {

    @Autowired
    private ProductFavoriteService productFavoriteService;

    @Operation(summary = "根据用户ID查询商品收藏", description = "获取指定用户的所有收藏商品")
    @GetMapping("/user/{userId}")
    public  Result<List<ProductFavorite>> getFavoritesByUserId(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("根据用户ID查询商品收藏请求: userId={}", userId);
        List<ProductFavorite> favorites = productFavoriteService.selectByUserId(userId);
        log.info("根据用户ID查询商品收藏成功: userId={}, count={}", userId, favorites.size());
        return  Result.success(favorites);
    }

    @Operation(summary = "分页查询商品收藏", description = "管理员分页查询所有商品收藏")
    @GetMapping("/list")
    public  Result<IPage<ProductFavorite>> getFavoriteList(
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询商品收藏请求: page={}, size={}", page, size);
        IPage<ProductFavorite> pageParam = new Page<>(page, size);
        IPage<ProductFavorite> result = productFavoriteService.selectPage(pageParam);
        log.info("分页查询商品收藏成功: totalPages={}, totalRecords={}", result.getPages(), result.getTotal());
        return  Result.success(result);
    }

    @Operation(summary = "根据ID查询商品收藏", description = "获取特定收藏的详细信息")
    @GetMapping("/{id}")
    public  Result<ProductFavorite> getFavoriteById(
            @Parameter(description = "收藏ID", required = true) @PathVariable Long id) {
        log.info("根据ID查询商品收藏请求: id={}", id);
        ProductFavorite favorite = productFavoriteService.selectById(id);
        if (favorite != null) {
            log.info("根据ID查询商品收藏成功: id={}", id);
            return  Result.success(favorite);
        } else {
            log.warn("根据ID查询商品收藏失败: id={}, 收藏不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "收藏不存在");
        }
    }

    @Operation(summary = "新增商品收藏", description = "用户收藏商品")
    @PostMapping("/add")
    public  Result<Boolean> addProductFavorite(@Valid @RequestBody ProductFavorite productFavorite) {
        log.info("新增商品收藏请求: userId={}, productId={}", 
                productFavorite.getUserId(), productFavorite.getProductId());
        
        // 验证用户只能添加自己的收藏
        if (!isCurrentUserOrAdmin(productFavorite.getUserId())) {
            log.warn("新增商品收藏失败: 权限不足, userId={}", productFavorite.getUserId());
            return  Result.failed(ResultCode.FORBIDDEN, "无权为其他用户添加收藏");
        }
        
        // 检查是否已收藏
        ProductFavorite existingFavorite = productFavoriteService.checkFavorite(
                productFavorite.getUserId(), productFavorite.getProductId());
        if (existingFavorite != null) {
            log.warn("新增商品收藏失败: 已收藏该商品, userId={}, productId={}", 
                    productFavorite.getUserId(), productFavorite.getProductId());
            return  Result.failed(ResultCode.FAILED, "已收藏该商品");
        }
        
        boolean result = productFavoriteService.insertProductFavorite(productFavorite);
        if (result) {
            log.info("新增商品收藏成功: userId={}, productId={}, id={}", 
                    productFavorite.getUserId(), productFavorite.getProductId(), productFavorite.getId());
            return  Result.success(true);
        } else {
            log.warn("新增商品收藏失败: userId={}, productId={}", 
                    productFavorite.getUserId(), productFavorite.getProductId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "更新商品收藏", description = "更新收藏信息（如分组、备注等）")
    @PutMapping("/update")
    public  Result<Boolean> updateProductFavorite(@Valid @RequestBody ProductFavorite productFavorite) {
        log.info("更新商品收藏请求: id={}", productFavorite.getId());
        
        // 参数验证
        if (productFavorite.getId() == null) {
            log.warn("更新商品收藏失败: ID不能为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "收藏ID不能为空");
        }
        
        boolean result = productFavoriteService.updateProductFavorite(productFavorite);
        if (result) {
            log.info("更新商品收藏成功: id={}", productFavorite.getId());
            return  Result.success(true);
        } else {
            log.warn("更新商品收藏失败: id={}", productFavorite.getId());
            return  Result.failed(ResultCode.FAILED);
        }
    }

    @Operation(summary = "根据ID删除商品收藏", description = "取消收藏商品")
    @DeleteMapping("/{id}")
    public  Result<Boolean> deleteProductFavorite(
            @Parameter(description = "收藏ID", required = true) @PathVariable Long id) {
        log.info("删除商品收藏请求: id={}", id);
        
        // 检查收藏是否存在
        ProductFavorite favorite = productFavoriteService.selectById(id);
        if (favorite == null) {
            log.warn("删除商品收藏失败: id={}, 收藏不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "收藏不存在");
        }
        
        boolean result = productFavoriteService.deleteProductFavorite(id);
        if (result) {
            log.info("删除商品收藏成功: id={}", id);
            return  Result.success(true);
        } else {
            log.warn("删除商品收藏失败: id={}", id);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "检查用户是否已收藏商品", description = "验证用户是否已收藏特定商品")
    @GetMapping("/check")
    public  Result<ProductFavorite> checkFavorite(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "商品ID") @RequestParam Long productId) {
        log.info("检查用户是否已收藏商品请求: userId={}, productId={}", userId, productId);
        
        // 验证用户只能查询自己的收藏状态
        if (!isCurrentUserOrAdmin(userId)) {
            log.warn("检查用户是否已收藏商品失败: 权限不足, userId={}", userId);
            return  Result.failed(ResultCode.FORBIDDEN, "无权查询其他用户的收藏状态");
        }
        
        ProductFavorite favorite = productFavoriteService.checkFavorite(userId, productId);
        log.info("检查用户是否已收藏商品成功: userId={}, productId={}, isFavorited={}", userId, productId, favorite != null);
        return  Result.success(favorite);
    }
    
    @Operation(summary = "切换收藏状态", description = "收藏或取消收藏商品")
    @PostMapping("/toggle")
    public  Result<Boolean> toggleFavorite(@RequestBody Map<String, Long> params) {
        Long userId = params.get("userId");
        Long productId = params.get("productId");
        
        log.info("切换收藏状态请求: userId={}, productId={}", userId, productId);
        
        // 参数验证
        if (userId == null || productId == null) {
            log.warn("切换收藏状态失败: 参数无效, userId={}, productId={}", userId, productId);
            return  Result.failed(ResultCode.VALIDATE_FAILED, "用户ID和商品ID不能为空");
        }
        
        // 验证用户只能操作自己的收藏
        if (!isCurrentUserOrAdmin(userId)) {
            log.warn("切换收藏状态失败: 权限不足, userId={}", userId);
            return  Result.failed(ResultCode.FORBIDDEN, "无权操作其他用户的收藏");
        }
        
        boolean isFavorited = productFavoriteService.toggleFavorite(userId, productId);
        log.info("切换收藏状态成功: userId={}, productId={}, result={}", userId, productId, isFavorited ? "已收藏" : "已取消收藏");
        return  Result.success(isFavorited);
    }
    
    @Operation(summary = "根据收藏夹查询收藏商品", description = "获取用户指定收藏夹中的商品")
    @GetMapping("/folder")
    public  Result<List<ProductFavorite>> selectByFolder(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "收藏夹ID") @RequestParam(required = false) Long folderId) {
        log.info("根据收藏夹查询收藏商品请求: userId={}, folderId={}", userId, folderId);
        List<ProductFavorite> favorites = productFavoriteService.selectByFolder(userId, folderId);
        log.info("根据收藏夹查询收藏商品成功: userId={}, folderId={}, count={}", userId, folderId, favorites.size());
        return  Result.success(favorites);
    }
    
    @Operation(summary = "获取用户的收藏夹列表", description = "获取用户创建的所有收藏夹")
    @GetMapping("/folders/{userId}")
    public  Result<List<Map<String, Object>>> getUserFolders(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取用户的收藏夹列表请求: userId={}", userId);
        List<Map<String, Object>> folders = productFavoriteService.getUserFolders(userId);
        log.info("获取用户的收藏夹列表成功: userId={}, count={}", userId, folders.size());
        return  Result.success(folders);
    }
    
    @Operation(summary = "创建收藏夹", description = "创建新的收藏夹")
    @PostMapping("/folder/create")
    public  Result<Boolean> createFolder(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        String folderName = params.get("folderName").toString();
        
        log.info("创建收藏夹请求: userId={}, folderName={}", userId, folderName);
        
        // 参数验证
        if (folderName == null || folderName.trim().isEmpty()) {
            log.warn("创建收藏夹失败: 文件夹名称无效");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "收藏夹名称不能为空");
        }
        
        // 验证用户只能为自己创建收藏夹
        if (!isCurrentUserOrAdmin(userId)) {
            log.warn("创建收藏夹失败: 权限不足, userId={}", userId);
            return  Result.failed(ResultCode.FORBIDDEN, "无权为其他用户创建收藏夹");
        }
        
        boolean result = productFavoriteService.createFolder(userId, folderName);
        if (result) {
            log.info("创建收藏夹成功: userId={}, folderName={}", userId, folderName);
            return  Result.success(true);
        } else {
            log.warn("创建收藏夹失败: userId={}, folderName={}", userId, folderName);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "移动商品到指定收藏夹", description = "将收藏的商品移动到指定的收藏夹")
    @PutMapping("/move")
    public  Result<Boolean> moveToFolder(@RequestBody Map<String, Long> params) {
        Long id = params.get("id");
        Long folderId = params.get("folderId");
        
        log.info("移动商品到指定收藏夹请求: id={}, folderId={}", id, folderId);
        
        // 检查收藏是否存在
        ProductFavorite favorite = productFavoriteService.selectById(id);
        if (favorite == null) {
            log.warn("移动商品到指定收藏夹失败: id={}, 收藏不存在", id);
            return  Result.failed(ResultCode.NOT_FOUND, "收藏不存在");
        }
        
        // 验证用户只能操作自己的收藏
        if (!isCurrentUserOrAdmin(favorite.getUserId())) {
            log.warn("移动商品到指定收藏夹失败: 权限不足, userId={}", favorite.getUserId());
            return  Result.failed(ResultCode.FORBIDDEN, "无权操作其他用户的收藏");
        }
        
        boolean result = productFavoriteService.moveToFolder(id, folderId);
        if (result) {
            log.info("移动商品到指定收藏夹成功: id={}, folderId={}", id, folderId);
            return  Result.success(true);
        } else {
            log.warn("移动商品到指定收藏夹失败: id={}, folderId={}", id, folderId);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "查询热门收藏商品", description = "获取被收藏最多的商品")
    @GetMapping("/hot")
    public  Result<List<Map<String, Object>>> getHotFavorites(
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("查询热门收藏商品请求: limit={}", limit);
        List<Map<String, Object>> hotFavorites = productFavoriteService.getHotFavorites(limit);
        log.info("查询热门收藏商品成功: count={}", hotFavorites.size());
        return  Result.success(hotFavorites);
    }
    
    @Operation(summary = "获取带商品信息的收藏列表", description = "获取用户收藏的商品详细信息")
    @GetMapping("/withInfo/{userId}")
    public  Result<List<ProductFavorite>> selectWithProductInfo(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("获取带商品信息的收藏列表请求: userId={}", userId);
        List<ProductFavorite> favorites = productFavoriteService.selectWithProductInfo(userId);
        log.info("获取带商品信息的收藏列表成功: userId={}, count={}", userId, favorites.size());
        return  Result.success(favorites);
    }
    
    @Operation(summary = "批量删除收藏", description = "批量删除多个收藏记录")
    @DeleteMapping("/batch")
    public  Result<Boolean> batchDelete(@RequestBody List<Long> ids) {
        log.info("批量删除收藏请求: ids={}", ids);
        
        if (ids == null || ids.isEmpty()) {
            log.warn("批量删除收藏失败: 参数无效, ids为空");
            return  Result.failed(ResultCode.VALIDATE_FAILED, "收藏ID列表不能为空");
        }
        
        boolean result = productFavoriteService.batchDelete(ids);
        if (result) {
            log.info("批量删除收藏成功: count={}", ids.size());
            return  Result.success(true);
        } else {
            log.warn("批量删除收藏失败: ids={}", ids);
            return  Result.failed(ResultCode.FAILED);
        }
    }
    
    @Operation(summary = "统计用户收藏商品数量", description = "获取用户收藏的商品总数")
    @GetMapping("/count/{userId}")
    public  Result<Integer> countUserFavorites(
            @Parameter(description = "用户ID", required = true) @PathVariable Long userId) {
        log.info("统计用户收藏商品数量请求: userId={}", userId);
        int count = productFavoriteService.countUserFavorites(userId);
        log.info("统计用户收藏商品数量成功: userId={}, count={}", userId, count);
        return  Result.success(count);
    }
    
    /**
     * 辅助方法：判断当前用户是否为管理员或操作用户本人
     */
    private boolean isCurrentUserOrAdmin(Long userId) {
        // 此处需要根据您的安全框架实现具体逻辑
        // 简化处理，实际应从SecurityContext中获取用户信息进行判断
        return true; // 默认允许，实际实现中应该返回正确的判断结果
    }
} 

