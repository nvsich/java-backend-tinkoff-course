package edu.java.scrapper.service.client.impl;

import edu.java.scrapper.dto.StackOverflowQuestionResponse;
import edu.java.scrapper.service.client.StackOverflowWebClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowWebClientImpl implements StackOverflowWebClient {

    private final WebClient webClient;
    private String baseUrl = "https://api.stackexchange.com/";

    public StackOverflowWebClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public StackOverflowWebClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override public Mono<StackOverflowQuestionResponse> fetchQuestion(String questionId) {
        return webClient.get().uri("/2.3/questions/{questionId}?site=stackoverflow", questionId).retrieve()
            .bodyToMono(StackOverflowQuestionResponse.class);
    }
}
