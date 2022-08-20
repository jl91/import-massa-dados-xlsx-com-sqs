package com.example.opensearchintegration.integration.aws.opensearch.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.admin.indices.alias.Alias;
import org.opensearch.action.admin.indices.delete.DeleteIndexRequest;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchType;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.settings.Settings;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class OpenSearchBaseService<T> {

    private RestHighLevelClient client;

    private ObjectMapper objectMapper;

    private String indexName;

    private Class clazz;

    public OpenSearchBaseService(
            final RestHighLevelClient client,
            final ObjectMapper objectMapper,
            final String indexName,
            final Class<T> clazz
    ) throws IOException {
        this.client = client;
        this.objectMapper = objectMapper;
        this.indexName = indexName;
        this.clazz = clazz;
        if (!this.hasIndex()) {
            this.createIndex();
        }
    }

    public List<T> findAllDocuments(
            final BigInteger page,
            final BigInteger size

    ) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.from(page.intValue());
        sourceBuilder.size(size.intValue());

        return this.searchDocuments(
                sourceBuilder
        );
    }

    public T findDocumentById(
            final String oriId
    ) throws IOException {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchPhraseQuery("_id", oriId));
        final var documents = this.searchDocuments(
                sourceBuilder
        );

        return documents.isEmpty()
                ? null :
                documents.get(0);
    }

    public List<T> searchDocuments(
            final SearchSourceBuilder sourceBuilder
    ) throws IOException {

        final var searchRequest = new SearchRequest(indexName);
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest.source(sourceBuilder);

        final var searchResponse = client.search(
                searchRequest,
                RequestOptions.DEFAULT
        );

        List<T> documents = Stream.of(searchResponse.getHits().getHits())
                .map(searchHint -> {
                    final var document = (T) objectMapper.convertValue(
                            searchHint.getSourceAsMap(),
                            this.clazz
                    );

//                    document.setUuid(searchHint.getId());
                    return document;
                })
                .collect(Collectors.toList());
        return documents;
    }

    public int save(
            final T document
    ) throws IOException {

        log.info("Add documents of {}", this.indexName);

        final var docMapper = objectMapper.convertValue(document, Map.class);

        final var indexRequest = new IndexRequest(
                this.indexName
        )
                .source(docMapper);

        final var bulkRequest = new BulkRequest();
        bulkRequest.add(indexRequest);

        final var response = client.bulk(
                bulkRequest,
                RequestOptions.DEFAULT
        );

        return response.getItems().length;
    }

    public int saveAll(
            final List<T> documents
    ) throws IOException {

        log.info("Add documents of {}", this.indexName);

        if (documents == null || documents.isEmpty()) {
            return 0;
        }

        final var documentsMappers = new ArrayList<Map<String, Object>>();

        for (T document : documents) {
            final var documentMap = objectMapper.convertValue(document, Map.class);
            documentsMappers.add(documentMap);
        }

        return this.addDocumentsMap(documentsMappers);
    }

    private int addDocumentsMap(
            final List<Map<String, Object>> documentsMappers
    ) throws IOException {

        final var bulkRequest = new BulkRequest();

        for (Map<String, Object> documentMap : documentsMappers) {

            final var indexRequest = new IndexRequest(
                    this.indexName
            )
                    .source(documentMap);

            bulkRequest.add(indexRequest);
        }

        final var response = client.bulk(
                bulkRequest,
                RequestOptions.DEFAULT
        );

        return response.getItems().length;
    }

    protected boolean hasIndex() throws IOException {
        final var hasIndexRequest = new GetIndexRequest(this.indexName);
        return client.indices()
                .exists(
                        hasIndexRequest,
                        RequestOptions.DEFAULT
                );
    }

    protected void createIndex() throws IOException {

        final var createIndexRequest = new CreateIndexRequest(this.indexName);

        createIndexRequest.settings(
                Settings.builder()
                        .put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 0)
        );

        final var typeMapping = new HashMap<String, String>();
        typeMapping.put("type", "integer");

        final var ageMapping = new HashMap<String, Object>();
        ageMapping.put("age", typeMapping);

        final var mapping = new HashMap<String, Object>();
        mapping.put("properties", ageMapping);

        createIndexRequest.mapping(mapping);
        createIndexRequest.alias(new Alias(this.indexName + "_alias"));

        final var createIndexResponse = client.indices()
                .create(
                        createIndexRequest,
                        RequestOptions.DEFAULT
                );
    }

    protected void deleteIndex() throws IOException {
        final var deleteIndexRequest = new DeleteIndexRequest(this.indexName);
        final var deleteIndexResponse = client.indices()
                .delete(
                        deleteIndexRequest,
                        RequestOptions.DEFAULT
                );
    }

}
