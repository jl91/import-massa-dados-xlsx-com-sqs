package com.example.opensearchintegration.integration.aws.opensearch.services;

import com.example.opensearchintegration.integration.aws.opensearch.documents.LineDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LineDocumentsOpenSearchService extends OpenSearchBaseService<LineDocument> {

    @Autowired
    public LineDocumentsOpenSearchService(
            RestHighLevelClient client,
            ObjectMapper objectMapper
    ) throws IOException {
        super(
                client,
                objectMapper,
                "lines-documents",
                LineDocument.class
        );
    }


    public void clearIndex() throws IOException {
        super.deleteIndex();
        super.createIndex();
    }
}
