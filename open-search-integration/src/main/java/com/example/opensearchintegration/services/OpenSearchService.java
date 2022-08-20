package com.example.opensearchintegration.services;

import com.example.opensearchintegration.configuration.ApplicationProperties;
import com.example.opensearchintegration.integration.aws.opensearch.documents.LineDocument;
import com.example.opensearchintegration.integration.aws.s3.*;
import com.example.opensearchintegration.integration.aws.s3.Object;
import com.example.opensearchintegration.integration.aws.sqs.publishers.message.NewFileUploaded;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OpenSearchService {

    @Autowired
    CSVReader CSVReader;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    private QueueMessagingTemplate messagingTemplate;

//    @Autowired
//    RestHighLevelClient client;
//
//    public void createIndex(
//            final String name
//    ) throws IOException {
//
//        // Create a non-default index with custom settings and mappings.
//        final var createIndexRequest = new CreateIndexRequest(name);
//
//        createIndexRequest.settings(
//                Settings.builder() //Specify in the settings how many shards you want in the index.
//                .put("index.number_of_shards", 1)
//                .put("index.number_of_replicas", 0)
//        );
//        //Create a set of maps for the index's mappings.
//        HashMap<String, String> typeMapping = new HashMap<String,String>();
//        typeMapping.put("type", "integer");
//
//        HashMap<String, HashMap<String, String>> ageMapping = new HashMap<>();
//        ageMapping.put("age", typeMapping);
//
//        HashMap<String, HashMap<String, HashMap<String, String>>> mapping = new HashMap<>();
//        mapping.put("properties", ageMapping);
//        createIndexRequest.mapping(mapping);
//        createIndexRequest.alias(new Alias(name + "_alias"));
//
//        CreateIndexResponse createIndexResponse = client.indices()
//                .create(createIndexRequest, RequestOptions.DEFAULT);
//
//    }

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

        this.dispatchNewFileUploaded(fileName);

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

//    @Transactional(propagation = Propagation.REQUIRED)
    public void processFile(
            final String filepath
    ) throws FileNotFoundException {

        // só apaga a base atual se conseguir ler a base do Elastic Search, geralmente usa-se um
        log.info("Clean ElasticSearch started");
//        lineRepository.deleteAll();
        log.info("Clean ElasticSearch Done");

        log.info("Loading InMemoryDatabase started");
        final var inMemoryDatabase = CSVReader.readFile(filepath);
        log.info("Loading InMemoryDatabase done");

        log.info("Save inMemoryDatabase started");
//        lineRepository.saveAll(inMemoryDatabase);
        log.info("Save inMemoryDatabase done");

        final var iterator = inMemoryDatabase.subList(0, 10000)
                .iterator();
        var index = 0;

        final List<LineDocument> sublist = new ArrayList<>();

        while (iterator.hasNext()) {
            final var currentLine = iterator.next();

            sublist.add(currentLine);

            if (sublist.size() == 100) {
                log.info("Saving chunk {}", (index + 1) / 100);
//                lineRepository.saveAll(sublist);
                sublist.clear();
            }

            index++;
        }

        if (!sublist.isEmpty()) {
            log.info("Last lines {}");
//            lineRepository.saveAll(sublist);
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
    ) {
        final var newPage = page.isPresent() ? page.get() : 1;
        final var newSize = size.isPresent() ? size.get() : 10;

//        final var pageRequest = PageRequest.of(newPage.intValue(), newSize.intValue());


//        return lineRepository.findAll(pageRequest)
//                .getContent();
        return List.of(new LineDocument());
    }


}
