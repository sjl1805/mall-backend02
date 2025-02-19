package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Spring MVC 核心配置类
 * 实现WebMvcConfigurer接口用于自定义MVC配置
 * 包含跨域配置、消息转换器、静态资源处理等核心配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 跨域资源共享(CORS)全局配置
     * 作用：解决前后端分离架构中的跨域问题
     * 配置说明：
     * - 允许所有来源（生产环境应指定具体域名）
     * - 允许的HTTP方法：GET, POST, PUT, DELETE等
     * - 允许所有请求头
     * - 预检请求缓存时间1小时
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")        // 匹配所有路径
                .allowedOrigins("*")      // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*")      // 允许所有请求头
                .maxAge(3600);            // 预检请求缓存时间（秒）
    }

    /**
     * 配置HTTP消息转换器
     * 作用：自定义JSON序列化/反序列化方式
     * 实现功能：
     * - 使用自定义的ObjectMapper配置
     * - 支持Java8时间类型的正确处理
     * - 禁用日期时间戳格式，使用ISO-8601标准格式
     */
    @Override
    public void configureMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
    }

    /**
     * 自定义ObjectMapper Bean
     * @Primary 注解确保优先使用此配置
     * 配置特性：
     * 1. 注册JavaTimeModule：支持Java8时间类型（LocalDate等）的序列化
     * 2. 禁用日期转时间戳：将日期格式化为可读字符串（ISO格式）
     * 3. 确保JSON序列化时保留时间精度
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 支持Java8时间类型
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用时间戳格式
        return objectMapper;
    }

    /**
     * 静态资源处理器配置
     * 作用：映射Swagger UI的静态资源路径
     * 配置说明：
     * - 将/swagger-ui/**路径映射到类路径下的swagger资源
     * - resourceChain(false)表示不启用资源链缓存（开发模式建议配置）
     * 注意：生产环境可能需要调整资源缓存策略
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**") // 资源处理的URL模式
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/") // 资源实际位置
                .resourceChain(false); // 禁用资源链缓存
    }
} 