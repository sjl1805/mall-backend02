package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户服务配置类
 */
@Configuration
@RequiredArgsConstructor
public class UserServiceConfig {

    /**
     * 配置密码编码器
     *
     * @return 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 配置对象映射器
     *
     * @return 对象映射器
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
} 