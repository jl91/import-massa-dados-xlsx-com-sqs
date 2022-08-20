package com.example.opensearchintegration.integration.aws.opensearch.configurations;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
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
    AWSCredentialsProvider awsCredentialsProvider;

    @Autowired
    AwsClientCredentialsConfiguration awsClientCredentialsConfiguration;


//    @Bean
//    public RestTemplate restTemplate() {
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//        connectionManager.setMaxTotal(MAX_CONNECTIONS);
//        connectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);
//
//        final var httpClient = HttpClientBuilder.create()
//                .setConnectionManager(connectionManager)
//                .build();
//
//        RestTemplate restTemplate = new RestTemplateBuilder()
//                .setConnectTimeout(Duration.ofMillis(CONNECTION_TIMEOUT))
//                .setReadTimeout(Duration.ofMillis(CONNECTION_TIMEOUT))
//                .messageConverters(new StringHttpMessageConverter(), new MappingJackson2HttpMessageConverter())
//                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
//                .build();
//
////        restTemplate.getInterceptors().add(
////                new BasicAuthenticationInterceptor(
////                        this.tokenUsername,
////                        this.tokenPassword
////                )
////        );
//
//        return restTemplate;
//    }


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

        final var SERVICE_NAME = "es";

        final var signer = new AWS4Signer();
        signer.setServiceName(SERVICE_NAME);
        signer.setRegionName(awsClientCredentialsConfiguration.getRegion());

        final var builder = RestClient.builder(
                        new HttpHost(
                                url.getHost(),
                                url.getPort(),
                                url.getProtocol()
                        )
                )
                .setHttpClientConfigCallback(
//                        httpClientBuilder -> {
//                            return httpClientBuilder.addInterceptorFirst(
//                                    new AWSRequestSigningApacheInterceptor(
//                                            SERVICE_NAME,
//                                            signer,
//                                            awsCredentialsProvider
//                                    )
//
//                            );
//                        }
                        httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                );
        return new RestHighLevelClient(builder);
    }


}
