package edu.java.scrapper.service.client.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.dto.GitHubRepoResponse;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class GitHubWebClientImplTest {

    private WireMockServer wireMockServer;

    private GitHubWebClientImpl gitHubWebClientImpl;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();

        WebClient.Builder webClientBuilder = WebClient.builder();
        gitHubWebClientImpl = new GitHubWebClientImpl(webClientBuilder, wireMockServer.baseUrl());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("fetchRepo() should return Mono<GitHubRepoResponse> with correct data")
    void fetchRepo() {
        Long tId = 1L;
        String tRepoName = "tRepoName";
        String tFullName = "tFullName";
        String tOwnerName = "tOwnerName";
        Long tOwnerId = 2L;
        String tOwnerUrl = "tOwnerUrl";
        String tOwnerHtmlUrl = "tOwnerHtmlUrl";
        String tHtmlUrl = "tHtmlUrl";
        String tDescription = "tDescription";
        OffsetDateTime tCreatedAt = OffsetDateTime.parse("2024-02-15T14:57:46Z");
        OffsetDateTime tUpdatedAt = OffsetDateTime.parse("2024-02-15T15:08:26Z");

        String mockResponse = String.format(
            """
                {
                    "id": %d,
                    "name": "%s",
                    "full_name": "%s",
                    "owner": {
                        "login": "%s",
                        "id": %d,
                        "url": "%s",
                        "html_url": "%s"
                    },
                    "html_url": "%s",
                    "description": "%s",
                    "created_at": "%s",
                    "updated_at": "%s"
                }
                """
            , tId, tRepoName, tFullName, tOwnerName, tOwnerId, tOwnerUrl,
            tOwnerHtmlUrl, tHtmlUrl, tDescription, tCreatedAt, tUpdatedAt
        );

        wireMockServer.stubFor(get(WireMock.urlEqualTo("/repos/" + tOwnerName + "/" + tRepoName))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(mockResponse)));

        Mono<GitHubRepoResponse> actualResponse = gitHubWebClientImpl.fetchRepo(tOwnerName, tRepoName);

        StepVerifier.create(actualResponse)
            .expectNextMatches(response ->
                response.id().equals(tId) &&
                    response.name().equals(tRepoName) &&
                    response.fullName().equals(tFullName) &&
                    response.owner().login().equals(tOwnerName) &&
                    response.owner().id().equals(tOwnerId) &&
                    response.owner().url().equals(tOwnerUrl) &&
                    response.owner().htmlUrl().equals(tOwnerHtmlUrl) &&
                    response.htmlUrl().equals(tHtmlUrl) &&
                    response.description().equals(tDescription) &&
                    response.createdAt().equals(tCreatedAt) &&
                    response.updatedAt().equals(tUpdatedAt)
            )
            .verifyComplete();

    }
}
