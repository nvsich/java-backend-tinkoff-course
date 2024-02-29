package edu.java.scrapper.service.client;

import edu.java.scrapper.dto.GitHubRepoResponse;
import reactor.core.publisher.Mono;

public interface GitHubWebClient {
    Mono<GitHubRepoResponse> fetchRepo(String ownerName, String repoName);
}
