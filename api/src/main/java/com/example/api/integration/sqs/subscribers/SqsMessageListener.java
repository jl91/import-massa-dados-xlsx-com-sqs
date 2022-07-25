package com.example.api.integration.sqs.subscribers;

import com.example.api.integration.sqs.configurations.SQSQueues;
import com.example.api.integration.sqs.publishers.messages.LineToSave;
import com.example.api.integration.sqs.publishers.messages.NewFileUploaded;
import com.example.api.services.ExcellBaseService;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
public class SqsMessageListener {

    @Autowired
    SQSQueues sqsQueues;

    @Autowired
    ExcellBaseService excellBaseService;

    @SqsListener(value = "teste-import", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(String message,
                               @Header("SenderId") String senderId
    ) {
        System.out.println(String.format("message received %s %s", senderId, message));
    }


    @SqsListener(value = "new-file-uploaded", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveNewFileUploaded(
            final NewFileUploaded message
    ) throws IOException {
        System.out.println(String.format("New File Uploaded: message received %s", message));

        excellBaseService.processBaseExcellFromS3(
                message.getBucket(),
                message.getFileName()
        );

    }

    @SqsListener(value = "lines-to-save", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveLinesToSave(
            final List<LineToSave> message
    ) throws IOException {
        System.out.println(String.format("Line To Save %s", message));
        excellBaseService.saveLines(message);
    }
}
