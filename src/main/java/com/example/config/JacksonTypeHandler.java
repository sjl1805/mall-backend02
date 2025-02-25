package com.example.config;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.stereotype.Component;


import java.io.IOException;

/**
 * MyBatis-Plus JSON类型处理器
 */
@Component
public class JacksonTypeHandler extends AbstractJsonTypeHandler<Object> {
    private static final Log log = LogFactory.getLog(JacksonTypeHandler.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public JacksonTypeHandler() {
        super(Object.class);
    }

    @Override
    public Object parse(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, Object.class);
        } catch (IOException e) {
            log.error("解析JSON失败: " + json, e);
            return null;
        }
    }

    @Override
    public String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("转换JSON失败: " + obj, e);
            return null;
        }
    }
} 