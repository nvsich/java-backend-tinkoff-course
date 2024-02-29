package edu.java.scrapper.configuration;

import edu.java.scrapper.service.client.GitHubWebClient;
import edu.java.scrapper.service.client.impl.GitHubWebClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GitHubWebClientConfig {

    @Value("${provider.github.baseUrl}")
    private String githubBaseUrl;

    @Bean
    public GitHubWebClient gitHubWebClient(WebClient.Builder webClientBuilder) {
        return new GitHubWebClientImpl(webClientBuilder, githubBaseUrl);
    }
}
