package com.example.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logger.warn("未授权访问尝试: {} - {}", request.getRequestURI(), authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "认证失败: " + authException.getMessage());
    }
} 