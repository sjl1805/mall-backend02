package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.model.entity.FavoriteFolder;
import com.example.service.FavoriteFolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FavoriteFolder", description = "收藏夹的增删改查")
@RestController
@RequestMapping("/favoriteFolder")
public class FavoriteFolderController {

    @Autowired
    private FavoriteFolderService favoriteFolderService;

    @Operation(summary = "根据用户ID查询收藏夹")
    @GetMapping("/user/{userId}")
    public CommonResult<List<FavoriteFolder>> getFoldersByUserId(@PathVariable Long userId) {
        List<FavoriteFolder> folders = favoriteFolderService.selectByUserId(userId);
        return CommonResult.success(folders);
    }

    @Operation(summary = "分页查询收藏夹")
    @GetMapping("/list")
    public CommonResult<IPage<FavoriteFolder>> getFavoriteFolderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<FavoriteFolder> pageParam = new Page<>(page, size);
        return CommonResult.success(favoriteFolderService.selectPage(pageParam));
    }

    @Operation(summary = "根据ID查询收藏夹")
    @GetMapping("/{id}")
    public CommonResult<FavoriteFolder> getFavoriteFolderById(@PathVariable Long id) {
        FavoriteFolder folder = favoriteFolderService.selectById(id);
        return folder != null ? CommonResult.success(folder) : CommonResult.failed(ResultCode.NOT_FOUND);
    }

    @Operation(summary = "新增收藏夹")
    @PostMapping("/add")
    public CommonResult<Boolean> addFavoriteFolder(@Valid @RequestBody FavoriteFolder favoriteFolder) {
        boolean result = favoriteFolderService.insertFavoriteFolder(favoriteFolder);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "更新收藏夹")
    @PutMapping("/update")
    public CommonResult<Boolean> updateFavoriteFolder(@Valid @RequestBody FavoriteFolder favoriteFolder) {
        boolean result = favoriteFolderService.updateFavoriteFolder(favoriteFolder);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }

    @Operation(summary = "根据ID删除收藏夹")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteFavoriteFolder(@PathVariable Long id) {
        boolean result = favoriteFolderService.deleteFavoriteFolder(id);
        return result ? CommonResult.success(true) : CommonResult.failed(ResultCode.FAILED);
    }
} 