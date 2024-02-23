package edu.java.scrapper.service.client.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.dto.StackOverflowQuestionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class StackOverflowWebClientImplTest {

    private WireMockServer wireMockServer;

    private StackOverflowWebClientImpl stackOverflowWebClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();

        WebClient.Builder webClientBuilder = WebClient.builder();
        stackOverflowWebClient = new StackOverflowWebClientImpl(webClientBuilder, wireMockServer.baseUrl());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("fetchQuestion() should return Mono<StackOverflowQuestionResponse> with correct data")
    void fetchQuestion() {
        boolean tIsAnswered = false;
        long tAnswerCount = 0L;
        long tLastActivityDate = 100L;
        long tCreationDate = 101L;
        long tLastEditDate = 102L;
        long tQuestionId = 0L;
        String tLink = "tLink";
        String tTitle = "tTitle";

        OffsetDateTime tLastActivityDateOffset =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(tLastActivityDate), ZoneOffset.UTC);
        OffsetDateTime tCreationDateOffset =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(tCreationDate), ZoneOffset.UTC);
        OffsetDateTime tLastEditDateOffset =
                OffsetDateTime.ofInstant(Instant.ofEpochSecond(tLastEditDate), ZoneOffset.UTC);

        String mockResponse = String.format(
                """
                        {
                        "items": [
                            {
                            "is_answered": %b,
                            "answer_count": %d,
                            "last_activity_date": %d,
                            "creation_date": %d,
                            "last_edit_date": %d,
                            "question_id": %d,
                            "link": "%s",
                            "title": "%s"
                            }
                        ]
                        }
                        """
                , tIsAnswered, tAnswerCount, tLastActivityDate, tCreationDate,
                tLastEditDate, tQuestionId, tLink, tTitle
        );

        wireMockServer.stubFor(get(WireMock.urlEqualTo("/2.3/questions/" + tQuestionId + "?site=stackoverflow"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)));

        Mono<StackOverflowQuestionResponse> actualResponse =
                stackOverflowWebClient.fetchQuestion(String.valueOf(tQuestionId));

        StepVerifier.create(actualResponse)
                .expectNextMatches(response ->
                        response.itemList().size() == 1 &&
                                response.itemList().getFirst().isAnswered() == tIsAnswered &&
                                response.itemList().getFirst().answerCount().equals(tAnswerCount) &&
                                response.itemList().getFirst().lastActivityDate().equals(tLastActivityDateOffset) &&
                                response.itemList().getFirst().creationDate().equals(tCreationDateOffset) &&
                                response.itemList().getFirst().lastEditDate().equals(tLastEditDateOffset) &&
                                response.itemList().getFirst().questionId().equals(tQuestionId) &&
                                response.itemList().getFirst().link().equals(tLink) &&
                                response.itemList().getFirst().title().equals(tTitle)
                ).verifyComplete();
    }
}
