package com.example.security;

import com.example.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器 - 处理基于Token的认证流程
 * 
 * <p>主要功能：
 * 1. 拦截请求并解析JWT令牌
 * 2. 验证令牌有效性
 * 3. 加载用户详细信息
 * 4. 设置安全上下文
 * 
 * @author 学生姓名
 * @version 1.0
 * @since 2023/06
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    /**
     * 构造函数依赖注入
     */
    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils,
                                   UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 核心过滤逻辑
     * 
     * @apiNote 执行流程：
     * 1. 放行登录/注册端点
     * 2. 从请求头解析JWT令牌
     * 3. 验证令牌有效性
     * 4. 加载用户信息并设置认证上下文
     * 5. 处理令牌过期等异常情况
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws IOException, ServletException {
        // 放行无需认证的端点
        if (isPublicEndpoint(request)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            processJwtAuthentication(request);
        } catch (ExpiredJwtException ex) {
            handleTokenExpiration(response, ex);
            return;
        } catch (JwtException | IllegalArgumentException ex) {
            handleInvalidToken(response, ex);
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * 判断是否为公开端点
     */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/users/login") || 
               path.startsWith("/users/register") ||
               path.startsWith("/swagger-ui") || 
               path.startsWith("/v3/api-docs");
    }

    /**
     * 处理JWT认证流程
     */
    private void processJwtAuthentication(HttpServletRequest request) {
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateToken(jwt)) {
            setSecurityContext(request, jwt);
        }
    }

    /**
     * 设置安全上下文
     */
    private void setSecurityContext(HttpServletRequest request, String jwt) {
        String username = jwtUtils.getUsernameFromToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 处理令牌过期异常
     */
    private void handleTokenExpiration(HttpServletResponse response, 
                                      ExpiredJwtException ex) throws IOException {
        logger.warn("Token过期: {}", ex.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token已过期");
    }

    /**
     * 处理无效令牌异常
     */
    private void handleInvalidToken(HttpServletResponse response, 
                                   Exception ex) throws IOException {
        logger.warn("无效Token: {}", ex.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无效的Token");
    }

    /**
     * 从请求头解析JWT令牌
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    // 日志记录器
    private static final Logger logger = 
        org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);
} 