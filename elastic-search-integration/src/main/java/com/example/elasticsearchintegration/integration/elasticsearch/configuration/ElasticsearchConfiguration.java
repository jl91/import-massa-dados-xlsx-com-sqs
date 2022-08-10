package com.example.elasticsearchintegration.integration.elasticsearch.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.elasticsearchintegration.repositories")
@ComponentScan(basePackages = {"com.example.elasticsearchintegration.repositories"})
public class ElasticsearchConfiguration {

    @Autowired
    ElasticSearchProperties elasticSearchProperties;

    @Bean
    public RestHighLevelClient client() {

        final var uri = elasticSearchProperties.getUris();
        final var username = elasticSearchProperties.getUsername();
        final var password = elasticSearchProperties.getPassword();

        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo(uri)
                .withBasicAuth(username, password)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }
}
