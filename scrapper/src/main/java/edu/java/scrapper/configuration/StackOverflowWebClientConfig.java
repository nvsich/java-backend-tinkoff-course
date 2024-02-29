package edu.java.scrapper.configuration;

import edu.java.scrapper.service.client.StackOverflowWebClient;
import edu.java.scrapper.service.client.impl.StackOverflowWebClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class StackOverflowWebClientConfig {

    @Value("${provider.stackoverflow.baseUrl}")
    private String stackoverflowBaseUrl;

    @Bean
    public StackOverflowWebClient stackOverflowWebClient(WebClient.Builder webClientBuilder) {
        return new StackOverflowWebClientImpl(webClientBuilder, stackoverflowBaseUrl);
    }
}
