package edu.java.bot.api.client;

import edu.java.bot.api.dto.request.AddLinkRequest;
import edu.java.bot.api.dto.request.RemoveLinkRequest;
import edu.java.bot.api.dto.response.LinkResponse;
import edu.java.bot.api.dto.response.ListLinkResponse;
import reactor.core.publisher.Mono;

public interface ScrapperClient {
    Mono<Void> registerChat(Long id);

    Mono<Void> deleteChat(Long id);

    Mono<ListLinkResponse> getAllLinksForChat(Long tgChatId);

    Mono<LinkResponse> addLinkToChat(Long tgChatId, AddLinkRequest addLinkRequest);

    Mono<LinkResponse> deleteLinkForChat(Long tgChatId, RemoveLinkRequest removeLinkRequest);
}
