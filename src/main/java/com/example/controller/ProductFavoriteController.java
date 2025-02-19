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

@RestController
@RequestMapping("/users/{userId}/favorites")
@RequiredArgsConstructor
@Tag(name = "ProductFavorite", description = "商品收藏管理接口")
public class ProductFavoriteController {

    private final ProductFavoriteService productFavoriteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "添加商品收藏", description = "用户收藏商品到指定收藏夹")
    @ApiResponse(responseCode = "201", description = "收藏成功")
    public Result<Boolean> addFavorite(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "收藏信息",
                required = true,
                content = @Content(schema = @Schema(implementation = ProductFavoriteDTO.class))
            )
            @Valid @RequestBody ProductFavoriteDTO favoriteDTO) {
        return Result.success(productFavoriteService.addFavorite(userId, favoriteDTO));
    }

    @DeleteMapping("/{favoriteId}")
    @Operation(summary = "移除收藏", description = "用户删除指定收藏项")
    @ApiResponse(responseCode = "200", description = "移除成功")
    public Result<Boolean> removeFavorite(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏项ID", example = "1") @PathVariable @Min(1) Long favoriteId) {
        return Result.success(productFavoriteService.removeFavorite(userId, favoriteId));
    }

    @GetMapping("/folders/{folderId}")
    @Operation(summary = "分页查询收藏", description = "分页获取指定收藏夹中的商品")
    @ApiResponse(responseCode = "200", description = "成功返回收藏列表")
    public Result<IPage<ProductFavorite>> listFavorites(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1") @PathVariable @Min(1) Long folderId,
            @Valid ProductFavoritePageDTO queryDTO) {
        return Result.success(productFavoriteService.listFavorites(userId, folderId, queryDTO));
    }

    @PatchMapping("/move")
    @Operation(summary = "批量转移收藏", description = "将多个收藏项移动到其他收藏夹")
    @ApiResponse(responseCode = "200", description = "转移成功")
    public Result<Boolean> moveToFolder(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "目标收藏夹ID", example = "1") @RequestParam @Min(1) Long targetFolderId,
            @RequestBody List<@Min(1) Long> favoriteIds) {
        return Result.success(productFavoriteService.moveToFolder(userId, favoriteIds, targetFolderId));
    }

    @GetMapping("/count")
    @Operation(summary = "获取收藏总数", description = "查询用户的商品收藏总数")
    @ApiResponse(responseCode = "200", description = "成功返回数量")
    public Result<Integer> getFavoriteCount(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId) {
        return Result.success(productFavoriteService.getUserFavoriteCount(userId));
    }

    @GetMapping("/check")
    @Operation(summary = "检查收藏状态", description = "验证用户是否已收藏指定商品")
    @ApiResponse(responseCode = "200", description = "返回收藏状态")
    public Result<Boolean> checkFavoriteExists(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "商品ID", example = "1") @RequestParam @Min(1) Long productId) {
        return Result.success(productFavoriteService.checkFavoriteExists(userId, productId));
    }
} 