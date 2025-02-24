package com.example.mall.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // 通用安全方案配置
    private static final String SECURITY_SCHEME_NAME = "JWT";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("电商平台API文档")
                        .version("1.0.0")
                        .description("基于Spring Boot 3的电商平台后端接口文档"))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

    // 管理后台API分组
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("管理后台")
                .pathsToMatch("/api/admin/**")
                .packagesToScan("com.example.mall.modules.admin")
                .build();
    }

    // 用户端API分组
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户端")
                .pathsToMatch("/api/user/**")
                .packagesToScan("com.example.mall.modules.user")
                .build();
    }

    // 公共API分组
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("公共接口")
                .pathsToMatch("/api/public/**")
                .packagesToScan("com.example.mall.modules.public")
                .build();
    }
} 