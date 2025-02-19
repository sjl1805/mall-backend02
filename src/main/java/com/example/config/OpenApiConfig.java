package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 配置类
 * 
 * @author 31815
 * @description 配置Swagger文档信息：
 *              1. 接口文档基本信息
 *              2. JWT安全认证方案
 * @createDate 2025-02-18 23:43:48
 */
@Configuration
public class OpenApiConfig {

    /**
     * 自定义OpenAPI配置
     * @return 完整的OpenAPI配置对象
     */
    @Bean
    public OpenAPI mallOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("电商平台API文档")
                        .description("大学生毕业设计电商平台接口文档")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("开发者")
                                .email("developer@example.com")
                                .url("https://example.com")))
                .schemaRequirement("JWT", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .name("Authorization")
                        .in(SecurityScheme.In.HEADER))
                .addSecurityItem(new SecurityRequirement()
                        .addList("JWT"));
    }
} 