package com.example.opensearchintegration.integration.aws.opensearch.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OpenSearchProperties {

    @Value("${opensearch.hostname}")
    private String uris;
}
