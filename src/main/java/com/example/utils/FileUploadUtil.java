package com.example.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传工具类 - 负责处理文件上传相关操作
 * 
 * <p>本类提供静态方法实现以下核心功能：
 * 1. 生成唯一文件名防止重名覆盖
 * 2. 自动创建上传目录结构
 * 3. 安全保存上传文件
 * 4. 提供文件路径解析功能
 * 
 * @author 学生姓名
 * @version 1.0
 * @since 2023/06
 */
public class FileUploadUtil {
    
    /**
     * 上传文件存储目录（建议后期改为从配置文件读取）
     */
    private static final String UPLOAD_DIR = "uploads/";

    /**
     * 处理文件上传操作
     * 
     * @param file Spring MVC接收的上传文件对象
     * @return 生成的唯一文件名（包含原始文件名）
     * @throws IOException 当发生以下情况时抛出：
     *                     - 目录创建失败
     *                     - 文件存储失败
     *                     - 文件访问权限不足
     * 
     * @apiNote 方法执行流程：
     * 1. 生成UUID前缀的唯一文件名
     * 2. 检查并创建上传目录
     * 3. 将上传文件转存到目标路径
     * 4. 返回生成的文件名供后续使用
     */
    public static String uploadFile(MultipartFile file) throws IOException {
        // 生成唯一文件名（UUID + 原始文件名，避免重名冲突）
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 获取上传目录路径对象
        Path uploadPath = Paths.get(UPLOAD_DIR);
        
        // 创建目录（如果不存在），使用createDirectories保证创建完整路径
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 解析完整文件路径
        Path filePath = uploadPath.resolve(fileName);
        
        // 转存文件到目标位置（适合中小文件，大文件应考虑分块上传）
        file.transferTo(filePath.toFile());

        return fileName;
    }

    /**
     * 获取文件存储路径
     * 
     * @param fileName 通过uploadFile方法生成的文件名
     * @return 完整的文件路径对象
     * 
     * @apiNote 此方法主要用于配合FileDownloadUtil类
     * 实现文件下载时的路径定位功能
     */
    public static Path getFilePath(String fileName) {
        // 使用Path.resolve安全拼接路径，避免路径遍历漏洞
        return Paths.get(UPLOAD_DIR).resolve(fileName);
    }
} 