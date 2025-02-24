package com.example.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.io.File;

@RestController
@RequestMapping("/files")
@Tag(name = "File", description = "文件上传和下载")
public class FileController {

    @Value("${file.storage.path}")
    private String fileStoragePath; 

    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        // 处理文件上传逻辑
        return "文件上传成功: " + file.getOriginalFilename();
    }

    @GetMapping("/download/{filename}")
    @Operation(summary = "文件下载")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        String filePath = fileStoragePath + "/" + filename;
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        FileSystemResource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
} 