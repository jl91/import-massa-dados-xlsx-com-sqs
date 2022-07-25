package com.example.api.integration.sqs.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SQSQueues {

    @Value("${spring.application.sqs.queues.new-file-uploaded}")
    private String newFileUploaded;

    @Value("${spring.application.sqs.queues.lines-to-save}")
    private String linesToSave;
}
