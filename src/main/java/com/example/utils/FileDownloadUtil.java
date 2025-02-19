package com.example.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 文件下载工具类 - 负责处理文件下载相关操作
 * 
 * <p>本类提供静态方法用于构建符合HTTP协议的文件下载响应，包含以下功能：
 * 1. 验证文件是否存在
 * 2. 自动检测文件MIME类型
 * 3. 设置下载响应头
 * 4. 构建完整的文件下载响应实体
 * 
 * @author 学生姓名
 * @version 1.0
 * @since 2023/06
 */
public class FileDownloadUtil {
    
    /**
     * 准备文件下载响应实体
     * 
     * @param fileName 要下载的文件名称（需存在于上传目录）
     * @return ResponseEntity<Resource> 包含文件内容、响应头和元数据的完整响应实体
     * @throws IOException 当发生以下情况时抛出：
     *                     - 文件不存在
     *                     - 文件读取失败
     *                     - 文件类型检测失败
     * 
     * @apiNote 方法执行流程：
     * 1. 通过FileUploadUtil获取文件存储路径
     * 2. 检查文件物理存在性
     * 3. 将文件内容读取到字节数组
     * 4. 创建Spring Resource对象包装文件内容
     * 5. 设置Content-Disposition响应头触发浏览器下载行为
     * 6. 自动检测文件MIME类型（失败时默认使用application/octet-stream）
     * 7. 构建完整的HTTP响应实体
     */
    public static ResponseEntity<Resource> prepareFileResponse(String fileName) throws IOException {
        // 通过工具类获取文件存储路径（基于应用程序配置的存储目录）
        Path filePath = FileUploadUtil.getFilePath(fileName);

        // 文件存在性校验 - 防止处理不存在的文件
        if (!Files.exists(filePath)) {
            throw new IOException("文件未找到: " + fileName);
        }

        // 读取文件内容到字节数组（适合中小文件，大文件应考虑流式处理）
        byte[] fileContent = Files.readAllBytes(filePath);
        
        // 使用Spring的Resource接口包装文件内容
        Resource resource = new ByteArrayResource(fileContent);

        // 构建响应头
        HttpHeaders headers = new HttpHeaders();
        // 设置内容处置头，强制浏览器触发下载行为
        headers.add(HttpHeaders.CONTENT_DISPOSITION, 
                   "attachment; filename=\"" + fileName + "\"");

        // 自动检测文件类型（基于文件扩展名和系统注册类型）
        String contentType = Files.probeContentType(filePath);
        // 安全回退机制：当无法检测类型时使用通用二进制流类型
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        // 构建完整的响应实体
        return ResponseEntity.ok()
                .headers(headers)  // 设置自定义响应头
                .contentType(MediaType.parseMediaType(contentType))  // 设置准确的内容类型
                .body(resource);  // 注入文件内容资源
    }
} 