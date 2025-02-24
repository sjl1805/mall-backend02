package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.example.config.KaptchaProperties;

@SpringBootApplication
@MapperScan("com.example.mapper")
@EnableCaching
@EnableConfigurationProperties(KaptchaProperties.class)
public class MallBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallBackendApplication.class, args);
        System.out.println("启动成功");
        System.out.println("http://localhost:8080/api/swagger-ui.html");
    }

}
