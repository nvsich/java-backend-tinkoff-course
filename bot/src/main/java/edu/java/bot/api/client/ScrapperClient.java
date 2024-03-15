package edu.java.bot.api.client;

import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListLinkResponse;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.exception.IncorrectRequestParamsException;
import edu.java.bot.exception.LinkNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class ScrapperClient {

    private static final String TG_CHAT_URI = "/tg-chat/{id}";
    private static final String LINKS_URI = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private final WebClient webClient;

    public ScrapperClient(WebClient.Builder webClient, String baseUrl) {
        this.webClient = webClient.baseUrl(baseUrl).build();
    }

    public Mono<Void> registerChat(Long id) {
        return webClient.post()
            .uri(TG_CHAT_URI, id)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(error -> new IncorrectRequestParamsException(error.getDescription()))
            )
            .bodyToMono(Void.class);
    }

    public Mono<Void> deleteChat(Long id) {
        return webClient.method(HttpMethod.DELETE)
            .uri(TG_CHAT_URI, id)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(error -> new IncorrectRequestParamsException(error.getDescription()))
            )
            .onStatus(
                status -> status == HttpStatus.NOT_FOUND,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(error -> new ChatNotFoundException(error.getDescription()))
            )
            .bodyToMono(Void.class);
    }

    public Mono<ListLinkResponse> getAllLinksForChat(Long tgChatId) {
        return webClient.get()
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, tgChatId.toString())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(error -> new IncorrectRequestParamsException(error.getDescription()))
            )
            .bodyToMono(ListLinkResponse.class);
    }

    public Mono<LinkResponse> addLinkToChat(Long tgChatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, tgChatId.toString())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(addLinkRequest)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(error -> new IncorrectRequestParamsException(error.getDescription()))
            )
            .onStatus(
                status -> status == HttpStatus.NOT_FOUND,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(error -> new ChatNotFoundException(error.getDescription()))
            )
            .bodyToMono(LinkResponse.class);
    }

    public Mono<LinkResponse> deleteLinkForChat(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_URI)
            .header(TG_CHAT_ID_HEADER, tgChatId.toString())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(Mono.just(removeLinkRequest), RemoveLinkRequest.class)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.BAD_REQUEST,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(error -> new IncorrectRequestParamsException(error.getDescription()))
            )
            .onStatus(
                status -> status == HttpStatus.NOT_FOUND,
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(error -> new LinkNotFoundException(error.getDescription()))
            )
            .bodyToMono(LinkResponse.class);
    }
}
