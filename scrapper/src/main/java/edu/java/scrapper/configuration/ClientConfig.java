package edu.java.scrapper.configuration;

import edu.java.scrapper.service.client.GitHubWebClient;
import edu.java.scrapper.service.client.StackOverflowWebClient;
import edu.java.scrapper.service.client.impl.GitHubWebClientImpl;
import edu.java.scrapper.service.client.impl.StackOverflowWebClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    public GitHubWebClient gitHubWebClient(WebClient.Builder webClientBuilder) {
        return new GitHubWebClientImpl(webClientBuilder);
    }

    @Bean
    public StackOverflowWebClient stackOverflowWebClient(WebClient.Builder webClientBuilder) {
        return new StackOverflowWebClientImpl(webClientBuilder);
    }
}
