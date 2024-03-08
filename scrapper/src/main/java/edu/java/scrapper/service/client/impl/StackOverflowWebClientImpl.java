package edu.java.scrapper.service.client.impl;

import edu.java.scrapper.dto.response.StackOverflowQuestionResponse;
import edu.java.scrapper.service.client.StackOverflowWebClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowWebClientImpl implements StackOverflowWebClient {

    private final WebClient webClient;

    public StackOverflowWebClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<StackOverflowQuestionResponse> fetchQuestion(String questionId) {
        return webClient.get().uri("/2.3/questions/{questionId}?site=stackoverflow", questionId).retrieve()
            .bodyToMono(StackOverflowQuestionResponse.class);
    }
}
