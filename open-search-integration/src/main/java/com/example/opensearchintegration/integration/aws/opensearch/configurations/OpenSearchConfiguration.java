package com.example.opensearchintegration.integration.aws.opensearch.configurations;

import com.example.opensearchintegration.integration.aws.configuration.AwsClientCredentialsConfiguration;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

@Configuration
public class OpenSearchConfiguration {

    @Autowired
    OpenSearchProperties openSearchProperties;

    @Autowired
    AwsClientCredentialsConfiguration awsClientCredentialsConfiguration;

    @Bean
    public CredentialsProvider credentialsProvider() {
        final var  credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(
                        openSearchProperties.getUsername(),
                        openSearchProperties.getPassword()
                )
        );
        return credentialsProvider;
    }

    @Bean
    public RestHighLevelClient client(
            final CredentialsProvider credentialsProvider
    ) throws MalformedURLException {
        final var url = new URL(openSearchProperties.getHostname());
        final var builder = RestClient.builder(
                        new HttpHost(
                                url.getHost(),
                                url.getPort(),
                                url.getProtocol()
                        )
                )
                .setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                )
                ;
        return new RestHighLevelClient(builder);
    }

}
