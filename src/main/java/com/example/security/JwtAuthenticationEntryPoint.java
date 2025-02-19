package com.example.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT认证入口点 - 处理未认证请求的响应
 * 
 * <p>主要职责：
 * 1. 记录未授权访问尝试
 * 2. 返回标准化的401错误响应
 * 3. 统一认证失败处理逻辑
 * 
 * @author 学生姓名
 * @version 1.0
 * @since 2023/06
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * 处理未认证请求
     * 
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param authException 认证异常信息
     * 
     * @apiNote 执行流程：
     * 1. 记录警告日志（请求路径和异常信息）
     * 2. 设置HTTP 401状态码
     * 3. 返回标准错误信息
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String path = request.getRequestURI();
        String errorMsg = String.format("未授权访问尝试: %s - %s", path, authException.getMessage());
        
        logger.warn(errorMsg);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
                         "认证失败: " + authException.getMessage());
    }
} 