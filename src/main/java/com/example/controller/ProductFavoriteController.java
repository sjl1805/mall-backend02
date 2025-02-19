package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.favorite.ProductFavoriteDTO;
import com.example.model.dto.favorite.ProductFavoritePageDTO;
import com.example.model.entity.ProductFavorite;
import com.example.service.ProductFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品收藏管理控制器
 * 
 * @author 31815
 * @description 提供商品收藏全生命周期管理功能，包含：
 *              1. 收藏夹管理
 *              2. 收藏项操作
 *              3. 收藏数据分析
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/users/{userId}/favorites")
@RequiredArgsConstructor
@Tag(name = "ProductFavorite", description = "商品收藏管理接口体系")
public class ProductFavoriteController {

    private final ProductFavoriteService productFavoriteService;

    /**
     * 添加收藏（带重复校验）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param favoriteDTO 收藏信息：
     *                   - productId: 商品ID（必须）
     *                   - folderId: 收藏夹ID（必须）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - FAVORITE_DUPLICATE(10001): 重复收藏同一商品
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "收藏添加", description = "用户收藏商品到指定收藏夹接口")
    @ApiResponse(responseCode = "201", description = "收藏成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复收藏商品")
    public Result<Boolean> addFavorite(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "收藏信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductFavoriteDTO.class))
            )
            @Valid @RequestBody ProductFavoriteDTO favoriteDTO) {
        return Result.success(productFavoriteService.addFavorite(userId, favoriteDTO));
    }

    /**
     * 移除收藏（事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param favoriteId 收藏项ID（路径参数，必须大于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - FAVORITE_NOT_FOUND(10002): 收藏项不存在
     */
    @DeleteMapping("/{favoriteId}")
    @Operation(summary = "收藏移除", description = "用户删除指定收藏项接口")
    @ApiResponse(responseCode = "200", description = "移除成功")
    @ApiResponse(responseCode = "404", description = "收藏项不存在")
    public Result<Boolean> removeFavorite(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏项ID", example = "1", required = true)
            @PathVariable @Min(1) Long favoriteId) {
        return Result.success(productFavoriteService.removeFavorite(userId, favoriteId));
    }

    /**
     * 分页查询收藏（带缓存）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param folderId 收藏夹ID（路径参数，必须大于0）
     * @param queryDTO 分页参数：
     *                 - page: 当前页码（默认1）
     *                 - size: 每页数量（默认10，最大50）
     * @return 分页结果包装对象
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping("/folders/{folderId}")
    @Operation(summary = "收藏分页查询", description = "分页获取指定收藏夹中的商品接口")
    @ApiResponse(responseCode = "200", description = "成功返回收藏列表")
    public Result<IPage<ProductFavorite>> listFavorites(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1", required = true)
            @PathVariable @Min(1) Long folderId,
            @Valid ProductFavoritePageDTO queryDTO) {
        return Result.success(productFavoriteService.listFavorites(userId, folderId, queryDTO));
    }

    /**
     * 批量转移收藏（事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param targetFolderId 目标收藏夹ID（必须大于0）
     * @param favoriteIds 收藏项ID列表（最多50个）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - BATCH_LIMIT_EXCEEDED(10003): 超出批量处理限制
     */
    @PatchMapping("/move")
    @Operation(summary = "收藏批量转移", description = "将多个收藏项移动到其他收藏夹接口")
    @ApiResponse(responseCode = "200", description = "转移成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "413", description = "超出批量处理限制")
    public Result<Boolean> moveToFolder(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "目标收藏夹ID", example = "1", required = true)
            @RequestParam @Min(1) Long targetFolderId,
            @RequestBody List<@Min(1) Long> favoriteIds) {
        return Result.success(productFavoriteService.moveToFolder(userId, favoriteIds, targetFolderId));
    }

    /**
     * 获取收藏总数（带缓存）
     * @param userId 用户ID（路径参数，必须大于0）
     * @return 收藏总数
     * @implNote 结果缓存优化，有效期1小时
     */
    @GetMapping("/count")
    @Operation(summary = "收藏总数统计", description = "查询用户的商品收藏总数接口")
    @ApiResponse(responseCode = "200", description = "成功返回数量")
    public Result<Integer> getFavoriteCount(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId) {
        return Result.success(productFavoriteService.getUserFavoriteCount(userId));
    }

    /**
     * 检查收藏状态（带缓存）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param productId 商品ID（必须大于0）
     * @return 收藏状态（true-已收藏，false-未收藏）
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping("/check")
    @Operation(summary = "收藏状态检查", description = "验证用户是否已收藏指定商品接口")
    @ApiResponse(responseCode = "200", description = "返回收藏状态")
    public Result<Boolean> checkFavoriteExists(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "商品ID", example = "1", required = true)
            @RequestParam @Min(1) Long productId) {
        return Result.success(productFavoriteService.checkFavoriteExists(userId, productId));
    }
}