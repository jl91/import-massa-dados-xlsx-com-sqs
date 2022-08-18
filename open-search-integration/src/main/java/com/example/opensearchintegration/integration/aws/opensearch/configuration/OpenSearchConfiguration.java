package com.example.opensearchintegration.integration.aws.opensearch.configuration;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.example.opensearchintegration.integration.aws.configuration.AwsClientCredentialsConfiguration;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
//import org.opensearch.client.RestClient;
//import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.example.opensearchintegration.repositories")
//@ComponentScan(basePackages = {"com.example.opensearchintegration.repositories"})
public class OpenSearchConfiguration {

    @Autowired
    OpenSearchProperties openSearchProperties;

    @Autowired
    AWSCredentialsProvider awsCredentialsProvider;

    @Autowired
    AwsClientCredentialsConfiguration awsClientCredentialsConfiguration;


    private final static int MAX_CONNECTIONS = 10;
    private final static int MAX_CONNECTIONS_PER_ROUTE = 10;
    private final static int CONNECTION_TIMEOUT = 2000;


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


//    @Bean
//    public CredentialsProvider credentialsProvider(
//            final AWSCredentialsProvider awsCredentialsProvider
//    ) {
//        final var credentials = awsCredentialsProvider.getCredentials();
//        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(
//                AuthScope.ANY,
//                new UsernamePasswordCredentials(
//                        credentials.getAWSAccessKeyId(),
//                        credentials.getAWSSecretKey()
//                )
//        );
//
//        return credentialsProvider;
//    }

//    @Bean
//    public RestHighLevelClient client() throws MalformedURLException {
//        final var url = new URL(openSearchProperties.getUris());
//
//        final var SERVICE_NAME = "es";
//
//        final var signer = new AWS4Signer();
//        signer.setServiceName(SERVICE_NAME);
//        signer.setRegionName(awsClientCredentialsConfiguration.getRegion());
//
//        final var builder = RestClient.builder(
//                        new HttpHost(
//                                url.getHost(),
//                                url.getPort(),
//                                url.getProtocol()
//                        )
//                )
//                .setHttpClientConfigCallback(
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
//                );
//        return new RestHighLevelClient(builder);
//    }






    public RestHighLevelClient client() {

        final var SERVICE_NAME = "es";

        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(SERVICE_NAME);
        signer.setRegionName(awsClientCredentialsConfiguration.getRegion());

        final var credentials = awsCredentialsProvider.getCredentials();

        final var interceptor = new AWSRequestSigningApacheInterceptor(
                SERVICE_NAME,
                signer,
                awsCredentialsProvider
        );

        final var restClientBuilder = RestClient
                .builder(HttpHost.create(openSearchProperties.getUris()))
                .setHttpClientConfigCallback(httpClienteConfigCallback -> httpClienteConfigCallback.addInterceptorLast(interceptor));

        return new org.elasticsearch.client.RestHighLevelClient(restClientBuilder);
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }


}
