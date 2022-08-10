package com.example.elasticsearchintegration.integration.elasticsearch.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ElasticSearchProperties {

    @Value("${spring.elasticsearch.uris}")
    private String uris;

    @Value("${spring.elasticsearch.uris.username}")
    private String username;

    @Value("${spring.elasticsearch.uris.password}")
    private String password;
}
