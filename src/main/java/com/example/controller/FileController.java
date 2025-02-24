package com.example.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.FileSystemResource;

@RestController
@RequestMapping("/files")
@Tag(name = "File", description = "文件上传和下载")
public class FileController {

    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        // 处理文件上传逻辑
        return "文件上传成功: " + file.getOriginalFilename();
    }

    @GetMapping("/download/{filename}")
    @Operation(summary = "文件下载")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource fileResource = new FileSystemResource("path/file"); // 替换为实际的资源对象
        return ResponseEntity.ok().body(fileResource);
    }
} 