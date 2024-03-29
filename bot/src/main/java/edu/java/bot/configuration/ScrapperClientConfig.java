package edu.java.bot.configuration;

import edu.java.bot.api.client.ScrapperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ScrapperClientConfig {

    @Value("${client.scrapper.baseUrl}")
    private String scrapperBaseUrl;

    @Bean
    public ScrapperClient scrapperClient(WebClient.Builder webClientBuilder) {
        return new ScrapperClient(webClientBuilder, scrapperBaseUrl);
    }
}
