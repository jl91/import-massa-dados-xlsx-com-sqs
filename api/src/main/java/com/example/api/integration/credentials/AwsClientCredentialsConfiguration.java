package com.example.api.integration.credentials;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Getter
public class AwsClientCredentialsConfiguration {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;


    @Bean
    @Primary
    public AWSStaticCredentialsProvider getCrendentials() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                        getAccessKey(),
                        getSecretKey()
                )
        );
    }
}
