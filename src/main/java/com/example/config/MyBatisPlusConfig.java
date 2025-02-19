package com.example.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置类
 * 
 * @author 31815
 * @description 配置MyBatis-Plus插件：
 *              1. 分页插件
 *              2. 乐观锁插件
 * @createDate 2025-02-18 23:43:48
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 配置MyBatis-Plus插件
     * @return 插件拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件（MySQL方言）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        
        return interceptor;
    }
} 