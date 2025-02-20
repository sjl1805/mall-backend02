package com.example.controller;

import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.utils.FileDownloadUtil;
import com.example.utils.FileUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件管理控制器
 * 
 * @author 31815
 * @description 提供文件全生命周期管理功能，包含：
 *              1. 文件上传与存储
 *              2. 文件下载与访问
 * @createDate 2025-02-18 23:43:48
 */
@RestController
@RequestMapping("/files")
@Tag(name = "File", description = "文件管理接口体系")
public class FileController {

    /**
     * 文件上传（带格式校验）
     * @param file 上传文件：
     *            - 格式限制：jpg/png/pdf/docx
     *            - 大小限制：最大10MB
     * @return 存储后的文件名
     * @throws com.example.exception.BusinessException 可能异常：
     *         - FILE_TYPE_INVALID(13001): 文件类型不合法
     *         - FILE_SIZE_EXCEED(13002): 文件大小超限
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "上传单个文件接口（支持格式：jpg/png/pdf/docx）")
    @ApiResponse(responseCode = "200", description = "文件上传成功")
    @ApiResponse(responseCode = "400", description = "文件校验失败")
    @ApiResponse(responseCode = "500", description = "服务器内部错误")
    public Result<String> uploadFile(
            @Parameter(description = "上传文件", required = true, 
                      schema = @Schema(type = "string", format = "binary"))
            @RequestParam("file") MultipartFile file) {
        try {
            String fileName = FileUploadUtil.uploadFile(file);
            return Result.success(fileName)
                    .message("文件上传成功: " + fileName);
        } catch (IOException e) {
            return Result.<String>error(ResultCode.INTERNAL_ERROR.getCode(), "文件上传失败: " + e.getMessage())
                    .debugInfo(e.getMessage());
        }
    }

    /**
     * 文件下载（带缓存控制）
     * @param fileName 文件名（需包含扩展名）
     * @return 文件字节流
     * @implNote 结果缓存优化，有效期24小时
     */
    @GetMapping("/download/{fileName}")
    @Operation(summary = "文件下载", description = "根据文件名下载文件接口")
    @ApiResponse(responseCode = "200", description = "文件下载成功", 
                content = @Content(schema = @Schema(type = "string", format = "binary")))
    @ApiResponse(responseCode = "404", description = "文件未找到")
    public ResponseEntity<?> downloadFile(
            @Parameter(description = "文件名", example = "backend.png", required = true)
            @PathVariable String fileName) {
        try {
            return FileDownloadUtil.prepareFileResponse(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Result.error(ResultCode.FILE_NOT_FOUND)
                            .message("文件未找到: " + fileName)
                            .debugInfo(e.getMessage()));
        }
    }
} 