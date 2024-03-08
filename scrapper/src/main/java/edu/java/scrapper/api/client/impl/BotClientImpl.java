package edu.java.scrapper.api.client.impl;

import edu.java.scrapper.api.client.BotClient;
import edu.java.scrapper.dto.request.LinkUpdateRequest;
import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.exception.IncorrectRequestParamsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClientImpl implements BotClient {

    private final WebClient webClient;

    public BotClientImpl(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<Void> sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        return webClient.post()
            .uri("/updates")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(linkUpdateRequest)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(error -> new IncorrectRequestParamsException(error.getDescription()))
            )
            .bodyToMono(Void.class);
    }
}
