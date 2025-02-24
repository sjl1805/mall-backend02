package com.example.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mall Backend API")
                        .version("1.0")
                        .description("API documentation for the Mall Backend application")
                        .contact(new Contact()
                                .name("开发者姓名")
                                .email("developer@example.com")))
                .components(new io.swagger.v3.oas.models.Components())
                .openapi("3.0.0");
    }
} 