package com.example.opensearchintegration.integration.aws.sqs.subscribers;

import com.example.opensearchintegration.integration.aws.sqs.publishers.message.NewFileUploaded;
import com.example.opensearchintegration.services.OpenSearchService;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SqsMessageListener {

    @Autowired
    OpenSearchService openSearchService;

    @SqsListener(value = "new-file-uploaded", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(
            final NewFileUploaded message,
            @Header("SenderId") final String senderId
    ) throws IOException {

        final var record = message.getRecords().get(0);
        final var filepath = record.getS3().getObject().getKey();

        openSearchService.processFile(filepath);

        log.info("new-file-uploaded message received {} {}", senderId, message);
    }
}
