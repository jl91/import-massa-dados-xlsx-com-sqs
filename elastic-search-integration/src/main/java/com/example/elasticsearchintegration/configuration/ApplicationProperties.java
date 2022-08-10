package com.example.elasticsearchintegration.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ApplicationProperties {

    @Value("${spring.application.store}")
    private String fileStore;

    @Value("${spring.application.sqs.queues.new-file-uploaded}")
    private String newFileUploaded;
}
