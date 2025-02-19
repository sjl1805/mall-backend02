package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.favorite.FavoriteFolderDTO;
import com.example.model.dto.favorite.FavoriteFolderPageDTO;
import com.example.model.entity.FavoriteFolder;
import com.example.service.FavoriteFolderService;
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
@RequestMapping("/users/{userId}/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite", description = "收藏夹管理接口")
public class FavoriteFolderController {

    private final FavoriteFolderService favoriteFolderService;

    @PostMapping("/folders")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建收藏夹", description = "用户创建新的收藏夹")
    @ApiResponse(responseCode = "201", description = "收藏夹创建成功")
    public Result<Boolean> createFolder(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "收藏夹信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FavoriteFolderDTO.class))
            )
            @Valid @RequestBody FavoriteFolderDTO folderDTO) {
        return Result.success(favoriteFolderService.createFolder(userId, folderDTO));
    }

    @GetMapping("/folders")
    @Operation(summary = "获取用户收藏夹", description = "查询用户的所有收藏夹列表")
    @ApiResponse(responseCode = "200", description = "成功返回收藏夹列表")
    public Result<List<FavoriteFolder>> getUserFolders(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId) {
        return Result.success(favoriteFolderService.getUserFolders(userId));
    }

    @PutMapping("/folders/{folderId}")
    @Operation(summary = "更新收藏夹信息", description = "修改收藏夹名称和描述")
    @ApiResponse(responseCode = "200", description = "信息更新成功")
    public Result<Boolean> updateFolder(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1") @PathVariable @Min(1) Long folderId,
            @Valid @RequestBody FavoriteFolderDTO folderDTO) {
        return Result.success(favoriteFolderService.updateFolder(userId, folderId, folderDTO));
    }

    @PatchMapping("/folders/{folderId}/public")
    @Operation(summary = "设置公开状态", description = "设置收藏夹是否公开")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    public Result<Boolean> updatePublicStatus(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1") @PathVariable @Min(1) Long folderId,
            @Parameter(description = "公开状态：0-私有 1-公开", example = "1")
            @RequestParam @NotNull @Min(0) @Max(1) Integer isPublic) {
        return Result.success(favoriteFolderService.updatePublicStatus(userId, folderId, isPublic));
    }

    @PatchMapping("/folders/{folderId}/sort")
    @Operation(summary = "调整排序位置", description = "修改收藏夹的显示顺序")
    @ApiResponse(responseCode = "200", description = "排序更新成功")
    public Result<Boolean> updateSort(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1") @PathVariable @Min(1) Long folderId,
            @Parameter(description = "新的排序值", example = "2")
            @RequestParam @NotNull @Min(0) Integer newSort) {
        return Result.success(favoriteFolderService.updateSort(userId, folderId, newSort));
    }

    @DeleteMapping("/folders/{folderId}")
    @Operation(summary = "删除收藏夹", description = "删除指定收藏夹")
    @ApiResponse(responseCode = "200", description = "删除成功")
    public Result<Boolean> deleteFolder(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1") @PathVariable @Min(1) Long folderId) {
        return Result.success(favoriteFolderService.deleteFolder(userId, folderId));
    }

    @GetMapping("/public-folders")
    @Operation(summary = "浏览公开收藏夹", description = "分页查看公开的收藏夹")
    @ApiResponse(responseCode = "200", description = "成功返回公开收藏夹列表")
    public Result<IPage<FavoriteFolder>> listPublicFolders(
            @Parameter(description = "分页查询参数") @Valid FavoriteFolderPageDTO queryDTO) {
        return Result.success(favoriteFolderService.listPublicFolders(queryDTO));
    }

    @PatchMapping("/folders/{folderId}/item-count")
    @Operation(summary = "更新收藏数量", description = "调整收藏夹中的商品数量")
    @ApiResponse(responseCode = "200", description = "数量更新成功")
    public Result<Boolean> updateItemCount(
            @Parameter(description = "用户ID", example = "1") @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1") @PathVariable @Min(1) Long folderId,
            @Parameter(description = "数量变化值（正数增加，负数减少）", example = "1")
            @RequestParam @NotNull Integer delta) {
        return Result.success(favoriteFolderService.updateItemCount(userId, folderId, delta));
    }
} 