package com.example.config;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;


/**
 * Redis配置类
 *
 * <p>主要功能：
 * 1. 配置RedisTemplate序列化方式
 * 2. 配置缓存管理器
 * 3. 自定义安全对象的序列化处理
 *
 * <p>序列化策略：
 * - Key使用String序列化
 * - Value使用JSON序列化
 * - 支持Java8时间类型
 * - 处理SecurityUser和GrantedAuthority的特殊序列化需求
 */
@EnableCaching
@Configuration
public class RedisConfig {

    @Value("${spring.cache.redis.time-to-live}")
    private Duration ttl;  // 修改字段类型为Duration

    /**
     * 配置RedisTemplate
     *
     * @param factory Redis连接工厂
     * @return 配置好的RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        ObjectMapper mapper = createObjectMapper();
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(mapper, Object.class);

        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 配置缓存管理器
     *
     * @param factory Redis连接工厂
     * @return 缓存管理器实例
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        ObjectMapper mapper = createObjectMapper();
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(mapper, Object.class);

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)  // 直接使用Duration对象
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(serializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }

    /**
     * 创建定制化的ObjectMapper
     * 处理安全相关对象的序列化问题
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // 添加序列化支持
        //mapper.addMixIn(SecurityUser.class, SecurityUserMixin.class);
        //mapper.addMixIn(SimpleGrantedAuthority.class, GrantedAuthorityMixin.class);

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        return mapper;
    }

} 