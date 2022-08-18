package com.example.opensearchintegration.integration.aws.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AwsClientCredentialsConfiguration {

    @Value("${cloud.aws.region.static}")
    private String region;
}
