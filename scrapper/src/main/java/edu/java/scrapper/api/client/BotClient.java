package edu.java.scrapper.api.client;

import edu.java.scrapper.dto.request.LinkUpdateRequest;
import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.exception.IncorrectRequestParamsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClient {

    private final WebClient webClient;

    public BotClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
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
            .bodyToMono(Void.class)
            .block();
    }
}
