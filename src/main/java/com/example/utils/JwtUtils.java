package com.example.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT工具类 - 负责JSON Web Token的生成、验证和解析
 * 
 * <p>本类基于jjwt 0.12.x版本实现，主要功能包括：
 * 1. 安全密钥生成与管理
 * 2. JWT令牌的生成（包含用户身份和权限信息）
 * 3. JWT令牌的完整性验证
 * 4. 从有效令牌中提取用户信息
 * 
 * @author 学生姓名
 * @version 1.0
 * @since 2023/06
 */
@Component
public class JwtUtils {
    
    /**
     * JWT签名密钥（应从配置文件读取）
     * 
     * @apiNote 要求密钥长度至少64字符（512位），
     * 用于HS512算法签名
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT有效期（单位：毫秒）
     * 
     * @apiNote 建议根据安全要求设置合理有效期，
     * 示例值：3600000（1小时）
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成签名密钥
     * 
     * @return 符合HS512算法要求的密钥
     * @throws IllegalArgumentException 当密钥长度不足时抛出
     * 
     * @apiNote 安全要求：
     * 1. 密钥必须转换为UTF-8字节数组
     * 2. 字节长度必须≥64（512位）
     * 3. 使用Keys类安全生成密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        // 强制密钥长度检查，防止弱密钥攻击
        if (keyBytes.length < 64) {
            throw new IllegalArgumentException("密钥长度必须至少为512位（64字符）");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成JWT令牌
     * 
     * @param username 用户标识（通常为用户名）
     * @param role 用户权限角色
     * @return 签名的JWT字符串
     * 
     * @apiNote 令牌包含以下声明：
     * - sub: 用户主体
     * - role: 用户角色
     * - iat: 签发时间
     * - exp: 过期时间
     */
    public String generateToken(String username, Integer role) {
        return Jwts.builder()
                .claim("role", role)  // 自定义声明：用户角色
                .subject(username)    // 标准声明：主题
                .issuedAt(new Date()) // 签发时间
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 过期时间
                .signWith(getSigningKey(), Jwts.SIG.HS512) // 使用HS512算法签名
                .compact();
    }

    /**
     * 验证JWT令牌有效性
     * 
     * @param token 待验证的JWT字符串
     * @return 验证结果：
     *         true - 令牌有效且未被篡改
     *         false - 令牌无效/过期/被篡改
     * 
     * @apiNote 验证过程包括：
     * 1. 签名验证
     * 2. 过期时间检查
     * 3. 令牌结构完整性检查
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey()) // 设置验证密钥
                    .build()
                    .parseSignedClaims(token);   // 解析并验证签名
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 捕获以下异常：
            // - 签名不匹配
            // - 令牌过期
            // - 令牌格式错误
            return false;
        }
    }

    /**
     * 从JWT令牌中提取用户名
     * 
     * @param token 有效的JWT字符串
     * @return 用户主体信息（用户名）
     * @throws JwtException 当令牌无效时抛出
     * 
     * @apiNote 需先通过validateToken验证令牌有效性
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // 设置验证密钥
                .build()
                .parseSignedClaims(token)     // 解析令牌
                .getPayload()                // 获取有效载荷
                .getSubject();               // 提取主题信息
    }
} 