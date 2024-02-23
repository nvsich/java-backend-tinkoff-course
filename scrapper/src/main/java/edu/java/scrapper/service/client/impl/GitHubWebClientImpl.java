package edu.java.scrapper.service.client.impl;

import edu.java.scrapper.dto.GitHubRepoResponse;
import edu.java.scrapper.service.client.GitHubWebClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubWebClientImpl implements GitHubWebClient {

    private final WebClient webClient;
    private String baseUrl = "https://api.github.com";

    public GitHubWebClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public GitHubWebClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override public Mono<GitHubRepoResponse> fetchRepo(String ownerName, String repoName) {
        return webClient.get().uri("/repos/{ownerName}/{repoName}", ownerName, repoName).retrieve()
            .bodyToMono(GitHubRepoResponse.class);
    }
}
