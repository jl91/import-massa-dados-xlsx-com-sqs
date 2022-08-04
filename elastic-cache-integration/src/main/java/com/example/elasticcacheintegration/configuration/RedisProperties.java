package com.example.elasticcacheintegration.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RedisProperties {

    @Value("${spring.application.redis.host}")
    private String host;

    @Value("${spring.application.redis.port}")
    private Integer port;

    @Value("${spring.application.redis.timeout-milliseconds}")
    private Integer timeoutMilliseconds;

    @Value("${spring.application.redis.password}")
    private String password;
}
