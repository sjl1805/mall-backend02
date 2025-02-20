package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Result;
import com.example.model.dto.FavoriteFolderDTO;
import com.example.model.dto.PageDTO;
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

/**
 * 收藏夹管理控制器
 * 
 * @author 31815
 * @description 提供收藏夹全生命周期管理功能，包含：
 *              1. 收藏夹创建与维护
 *              2. 收藏夹权限管理
 *              3. 收藏夹数据统计
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/users/{userId}/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite", description = "收藏夹管理接口体系")
public class FavoriteFolderController {

    private final FavoriteFolderService favoriteFolderService;

    /**
     * 创建收藏夹（带重复校验）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param folderDTO 收藏夹信息：
     *                 - folderName: 收藏夹名称（必须，最多20字）
     *                 - description: 描述信息（最多100字）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - FOLDER_DUPLICATE(14001): 重复收藏夹名称
     */
    @PostMapping("/folders")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "收藏夹创建", description = "用户创建新的收藏夹接口")
    @ApiResponse(responseCode = "201", description = "收藏夹创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "重复收藏夹名称")
    public Result<Boolean> createFolder(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "收藏夹信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FavoriteFolderDTO.class))
            )
            @Valid @RequestBody FavoriteFolderDTO folderDTO) {
        return Result.success(favoriteFolderService.createFolder(userId, folderDTO));
    }

    /**
     * 获取用户收藏夹（带缓存）
     * @param userId 用户ID（路径参数，必须大于0）
     * @return 收藏夹列表（按排序值升序）
     * @implNote 结果缓存优化，有效期1小时
     */
    @GetMapping("/folders")
    @Operation(summary = "用户收藏夹查询", description = "查询用户的所有收藏夹列表接口")
    @ApiResponse(responseCode = "200", description = "成功返回收藏夹列表")
    public Result<List<FavoriteFolderDTO>> getUserFolders(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId) {
        return Result.success(favoriteFolderService.getUserFolders(userId));
    }

    /**
     * 更新收藏夹信息（事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param folderId 收藏夹ID（路径参数，必须大于0）
     * @param folderDTO 更新信息：
     *                 - folderName: 新名称（可选）
     *                 - description: 新描述（可选）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - FOLDER_NOT_FOUND(14002): 收藏夹不存在
     */
    @PutMapping("/folders/{folderId}")
    @Operation(summary = "收藏夹信息更新", description = "修改收藏夹名称和描述接口")
    @ApiResponse(responseCode = "200", description = "信息更新成功")
    @ApiResponse(responseCode = "404", description = "收藏夹不存在")
    public Result<Boolean> updateFolder(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1", required = true)
            @PathVariable @Min(1) Long folderId,
            @Valid @RequestBody FavoriteFolderDTO folderDTO) {
        return Result.success(favoriteFolderService.updateFolder(userId, folderId, folderDTO));
    }

    /**
     * 设置公开状态（事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param folderId 收藏夹ID（路径参数，必须大于0）
     * @param isPublic 公开状态（0-私有，1-公开）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - FOLDER_NOT_FOUND(14002): 收藏夹不存在
     */
    @PatchMapping("/folders/{folderId}/public")
    @Operation(summary = "公开状态设置", description = "设置收藏夹是否公开接口")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "404", description = "收藏夹不存在")
    public Result<Boolean> updatePublicStatus(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1", required = true)
            @PathVariable @Min(1) Long folderId,
            @Parameter(description = "公开状态", schema = @Schema(allowableValues = {"0", "1"}), required = true)
            @RequestParam @NotNull @Min(0) @Max(1) Integer isPublic) {
        return Result.success(favoriteFolderService.updatePublicStatus(userId, folderId, isPublic));
    }

    /**
     * 调整排序位置（事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param folderId 收藏夹ID（路径参数，必须大于0）
     * @param newSort 新排序值（必须大于等于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - FOLDER_NOT_FOUND(14002): 收藏夹不存在
     */
    @PatchMapping("/folders/{folderId}/sort")
    @Operation(summary = "收藏夹排序调整", description = "修改收藏夹的显示顺序接口")
    @ApiResponse(responseCode = "200", description = "排序更新成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "404", description = "收藏夹不存在")
    public Result<Boolean> updateSort(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1", required = true)
            @PathVariable @Min(1) Long folderId,
            @Parameter(description = "新排序值", example = "2", required = true)
            @RequestParam @NotNull @Min(0) Integer newSort) {
        return Result.success(favoriteFolderService.updateSort(userId, folderId, newSort));
    }

    /**
     * 删除收藏夹（事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param folderId 收藏夹ID（路径参数，必须大于0）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - FOLDER_NOT_EMPTY(14003): 收藏夹非空
     */
    @DeleteMapping("/folders/{folderId}")
    @Operation(summary = "收藏夹删除", description = "删除指定收藏夹接口")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "400", description = "收藏夹非空")
    @ApiResponse(responseCode = "404", description = "收藏夹不存在")
    public Result<Boolean> deleteFolder(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1", required = true)
            @PathVariable @Min(1) Long folderId) {
        return Result.success(favoriteFolderService.deleteFolder(userId, folderId));
    }

    /**
     * 浏览公开收藏夹（带缓存）
     * @param queryDTO 分页参数：
     *                 - page: 当前页码（默认1）
     *                 - size: 每页数量（默认10，最大50）
     * @return 分页结果包装对象
     * @implNote 结果缓存优化，有效期30分钟
     */
    @GetMapping("/public-folders")
    @Operation(summary = "公开收藏夹浏览", description = "分页查看公开的收藏夹接口")
    @ApiResponse(responseCode = "200", description = "成功返回公开收藏夹列表")
    public Result<IPage<FavoriteFolderDTO>> listPublicFolders(
            @Parameter(description = "分页查询参数") @Valid PageDTO<FavoriteFolderDTO> queryDTO) {
        return Result.success(favoriteFolderService.listPublicFolders(queryDTO));
    }

    /**
     * 更新收藏数量（事务操作）
     * @param userId 用户ID（路径参数，必须大于0）
     * @param folderId 收藏夹ID（路径参数，必须大于0）
     * @param delta 数量变化值（允许正负值）
     * @return 操作结果
     * @throws com.example.exception.BusinessException 可能异常：
     *         - FOLDER_NOT_FOUND(14002): 收藏夹不存在
     */
    @PatchMapping("/folders/{folderId}/item-count")
    @Operation(summary = "收藏数量更新", description = "调整收藏夹中的商品数量接口")
    @ApiResponse(responseCode = "200", description = "数量更新成功")
    @ApiResponse(responseCode = "404", description = "收藏夹不存在")
    public Result<Boolean> updateItemCount(
            @Parameter(description = "用户ID", example = "1", required = true)
            @PathVariable @Min(1) Long userId,
            @Parameter(description = "收藏夹ID", example = "1", required = true)
            @PathVariable @Min(1) Long folderId,
            @Parameter(description = "数量变化值", example = "1", required = true)
            @RequestParam @NotNull Integer delta) {
        return Result.success(favoriteFolderService.updateItemCount(userId, folderId, delta));
    }
} 