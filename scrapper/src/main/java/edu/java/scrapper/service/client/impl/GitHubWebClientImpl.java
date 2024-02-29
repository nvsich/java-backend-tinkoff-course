package edu.java.scrapper.service.client.impl;

import edu.java.scrapper.dto.GitHubRepoResponse;
import edu.java.scrapper.service.client.GitHubWebClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubWebClientImpl implements GitHubWebClient {

    private final WebClient webClient;

    public GitHubWebClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<GitHubRepoResponse> fetchRepo(String ownerName, String repoName) {
        return webClient.get().uri("/repos/{ownerName}/{repoName}", ownerName, repoName).retrieve()
            .bodyToMono(GitHubRepoResponse.class);
    }
}
