package com.example.opensearchintegration.integration.aws.opensearch.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OpenSearchProperties {

    @Value("${opensearch.hostname}")
    private String hostname;

    @Value("${opensearch.username}")
    private String username;

    @Value("${opensearch.password}")
    private String password;
}
