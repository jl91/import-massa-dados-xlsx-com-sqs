package com.example.api.integration.credentials;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AwsS3Configuration {

    @Autowired
    AWSStaticCredentialsProvider awsStaticCredentialsProvider;

    @Autowired
    AwsClientCredentialsConfiguration awsClientCredentials;

    @Value("${cloud.aws.s3.endpoint}")
    String s3Endpoint;

    @Bean
    public AmazonS3 AutheticationS3() {

        log.info("Autenticando S3...");
        return AmazonS3ClientBuilder
                .standard()
//                .withRegion(awsClientCredentials.getRegion())
                .withCredentials(awsStaticCredentialsProvider)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        s3Endpoint,
                        awsClientCredentials.getRegion()
                ))
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
