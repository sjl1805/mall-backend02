package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis缓存配置类
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 自定义缓存配置（全局默认）
     * 1. 使用Jackson JSON序列化
     * 2. Key使用String序列化
     * 3. 默认缓存1小时
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration(ObjectMapper objectMapper) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
    }

    /**
     * 自定义RedisTemplate配置
     * 1. Key使用String序列化
     * 2. Value使用JSON序列化
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory factory,
            ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        GenericJackson2JsonRedisSerializer serializer = 
            new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 自定义缓存管理器
     * 1. 按缓存名称配置不同策略
     * 2. 用户服务缓存30分钟
     * 3. 统计信息缓存2小时
     */
    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory factory,
            ObjectMapper objectMapper) {
        
        Map<String, RedisCacheConfiguration> configs = new HashMap<>();
        
        // 用户服务缓存配置
        configs.put("userService", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .prefixCacheNameWith("user::"));

        // 统计信息缓存配置
        configs.put("userStats", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(2))
                .prefixCacheNameWith("stats::"));

        return RedisCacheManager.builder(factory)
                .withInitialCacheConfigurations(configs)
                .cacheDefaults(cacheConfiguration(objectMapper))
                .build();
    }
} 