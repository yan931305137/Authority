package com.authority.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用配置类，用于配置Jackson ObjectMapper。
 */
@Configuration
public class CommonConfiguration {
    /**
     * 配置ObjectMapper，设置序列化时忽略空值。
     * @return 配置好的ObjectMapper实例。
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
}
