package edu.java.scrapper.api.client;

import edu.java.scrapper.api.dto.request.LinkUpdateRequest;
import reactor.core.publisher.Mono;

public interface BotClient {

    Mono<Void> sendUpdate(LinkUpdateRequest linkUpdateRequest);
}