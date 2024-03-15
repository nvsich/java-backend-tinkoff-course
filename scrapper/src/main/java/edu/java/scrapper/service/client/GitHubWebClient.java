package edu.java.scrapper.service.client;

import edu.java.scrapper.dto.response.GitHubRepoResponse;

public interface GitHubWebClient {
    GitHubRepoResponse fetchRepo(String ownerName, String repoName);
}
