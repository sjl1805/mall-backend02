package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI mallOpenAPI() {
        return new OpenAPI()
                .openapi("3.0.3")
                .info(new Info()
                        .title("Mall API")
                        .version("1.0")
                        .description("电商平台接口文档")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("项目文档")
                        .url("https://github.com/your-repo"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("mall-public")
                .pathsToMatch("/api/**")
                .build();
    }
} 