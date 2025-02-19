package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI mallOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("电商平台API文档")
                        .description("大学生毕业设计电商平台接口文档")
                        .version("v1.0"))
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