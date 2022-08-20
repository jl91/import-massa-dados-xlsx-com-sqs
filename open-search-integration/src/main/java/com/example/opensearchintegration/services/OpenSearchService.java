package com.example.opensearchintegration.services;

import com.example.opensearchintegration.configuration.ApplicationProperties;
import com.example.opensearchintegration.integration.aws.opensearch.documents.LineDocument;
import com.example.opensearchintegration.integration.aws.opensearch.services.LineDocumentsOpenSearchService;
import com.example.opensearchintegration.integration.aws.s3.*;
import com.example.opensearchintegration.integration.aws.s3.Object;
import com.example.opensearchintegration.integration.aws.sqs.publishers.message.NewFileUploaded;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OpenSearchService {

    @Autowired
    private CSVReader CSVReader;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private QueueMessagingTemplate messagingTemplate;

    @Autowired
    private LineDocumentsOpenSearchService lineDocumentsOpenSearchService;

    public void saveFile(
            final MultipartFile multipartFile
    ) throws IOException {
        final var fileName = multipartFile.getOriginalFilename();
        final var path = applicationProperties.getFileStore() + File.separator + fileName;
        multipartFile.transferTo(
                new File(
                        path
                )
        );

//        this.dispatchNewFileUploaded(fileName);
        this.processFile(path);

    }

    private void dispatchNewFileUploaded(
            final String filename
    ) {
        final var message = getMessage(filename);
        messagingTemplate.convertAndSend(
                applicationProperties.getNewFileUploaded(),
                message
        );
    }

    private NewFileUploaded getMessage(
            final String filename
    ) {
        return (NewFileUploaded) new NewFileUploaded()
                .setRecords(
                        List.of(
                                new ObjectRecord()
                                        .setEventVersion("2.1")
                                        .setEventSource("aws:s3")
                                        .setEventSource("us-east-2")
                                        .setEventTime("2018-12-19T01:51:03.251Z")
                                        .setEventName("eventName")
                                        .setUserIdentity(
                                                new UserIdentity()
                                                        .setPrincipalId("AWS:AIDAIZLCFC5TZD36YHNZY")
                                        )
                                        .setRequestParameters(
                                                new RequestParameters()
                                                        .setSourceIPAddress("52.46.82.38")

                                        )
                                        .setResponseElements(
                                                new ResponseElements()
                                                        .setXAmzRequestId("6C05F1340AA50D21")
                                                        .setXAmzRequestId("9e8KovdAUJwmYu1qnEv+urrO8T0vQ+UOpkPnFYLE6agmJSn745/T3/tVs0Low/vXonTdATvW23M=")
                                        )
                                        .setS3(
                                                new S3()
                                                        .setS3SchemaVersion("1.0")
                                                        .setConfigurationId("test_SQS_Notification_1")
                                                        .setBucket(
                                                                new Bucket()
                                                                        .setName("myBucketName")
                                                                        .setOwnerIdentity(
                                                                                new OwnerIdentity()
                                                                                        .setPrincipalId("A2SGQBYRFBZET")
                                                                        )
                                                                        .setArn("arn:aws:s3:::myBucketName")
                                                        )
                                                        .setObject(
                                                                new Object()
                                                                        .setKey(applicationProperties.getFileStore() + File.separator + filename)
                                                                        .setSize(713)
                                                                        .setETag("1ff1209e4140b4ff7a9d2b922f57f486")
                                                                        .setSequencer("005C19A40717D99642")

                                                        )
                                        )
                        )
                );

    }

    public void processFile(
            final String filepath
    ) throws IOException {

        log.info("Clean OpenSearch started");
//        lineDocumentsOpenSearchService.clearIndex();
        log.info("Clean OpenSearch Done");

        log.info("Loading InMemoryDatabase started");
        final var inMemoryDatabase = CSVReader.readFile(filepath);
        log.info("Loading InMemoryDatabase done");

//        log.info("Save inMemoryDatabase started");
////        lineRepository.saveAll(inMemoryDatabase);
//        log.info("Save inMemoryDatabase done");

        final var iterator = inMemoryDatabase.subList(0, 10000)
                .iterator();
        var index = 0;

        final List<LineDocument> sublist = new ArrayList<>();

        while (iterator.hasNext()) {
            final var currentLine = iterator.next();

            sublist.add(currentLine);
            currentLine.setUuid(UUID.randomUUID().toString());
//            currentLine.setCreatedAt(LocalDate.now());

            if (sublist.size() == 100) {
                log.info("Saving chunk {}", (index + 1) / 100);
                final var totalResult = lineDocumentsOpenSearchService.saveAll(sublist);
                log.info("Total saved lines {}", totalResult);
                sublist.clear();
            }

            index++;
        }

        if (!sublist.isEmpty()) {
            log.info("Last lines {}");
            lineDocumentsOpenSearchService.saveAll(sublist);
            sublist.clear();
        }

        final var file = new File(filepath);
        if (file.exists() && file.canWrite()) {
            file.delete();
        }

    }


    public List<LineDocument> fetchAll(
            final Optional<BigInteger> page,
            final Optional<BigInteger> size
    ) throws IOException {
        final BigInteger newPage = page.isPresent() ? page.get() : BigInteger.valueOf(1);
        final BigInteger newSize = size.isPresent() ? size.get() : BigInteger.valueOf(10);

        return lineDocumentsOpenSearchService.findAllDocuments(newPage, newSize);
    }


}
