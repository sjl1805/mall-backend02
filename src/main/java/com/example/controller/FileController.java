package com.example.controller;

import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.utils.FileDownloadUtil;
import com.example.utils.FileUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@Tag(name = "File", description = "文件上传下载接口")
public class FileController {

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传单个文件，最大10MB")
    @ApiResponse(responseCode = "200", description = "文件上传成功")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = FileUploadUtil.uploadFile(file);
            return Result.success(fileName)
                    .message("文件上传成功: " + fileName);
        } catch (IOException e) {
            return Result.<String>error(ResultCode.INTERNAL_ERROR.getCode(), "文件上传失败: " + e.getMessage())
                    .debugInfo(e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    @Operation(summary = "下载文件", description = "根据文件名下载文件")
    @ApiResponse(responseCode = "200", description = "文件下载成功")
    @ApiResponse(responseCode = "404", description = "文件未找到")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        try {
            System.out.println("downloadFile:****************************** " + fileName);
            return FileDownloadUtil.prepareFileResponse(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Result.error(ResultCode.FILE_NOT_FOUND)
                            .message("文件未找到: " + fileName)
                            .debugInfo(e.getMessage()));
        }
    }
} 