package com.example.elasticsearchintegration.integration.aws.sqs.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.example.elasticsearchintegration.integration.aws.configuration.AwsClientCredentialsConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import java.util.Collections;

@Configuration
public class AwsSQSConfiguration {

    @Autowired
    AWSStaticCredentialsProvider awsStaticCredentialsProvider;

    @Autowired
    AwsClientCredentialsConfiguration awsClientCredentials;

    @Autowired
    MessageConverter messageConverter;

    @Value("${cloud.aws.sqs.endpoint}")
    private String endpoint;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder
                .standard()
//                .withRegion(Regions.fromName(region))
                .withCredentials(awsStaticCredentialsProvider)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                endpoint,
                                awsClientCredentials.getRegion()
                        )
                )
                .build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(
            AmazonSQSAsync amazonSQSAsync
    ) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(
            final AmazonSQSAsync amazonSQSAsync
    ) {

        final var queueHandlerFactory = new QueueMessageHandlerFactory();
        queueHandlerFactory.setAmazonSqs(amazonSQSAsync);

        queueHandlerFactory.setArgumentResolvers(
                Collections.singletonList(
                        new PayloadMethodArgumentResolver(
                                messageConverter
                        )
                )
        );
        return queueHandlerFactory;
    }


}
