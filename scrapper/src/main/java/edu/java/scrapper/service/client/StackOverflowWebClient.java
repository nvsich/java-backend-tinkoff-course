package edu.java.scrapper.service.client;

import edu.java.scrapper.dto.response.StackOverflowQuestionResponse;
import reactor.core.publisher.Mono;

public interface StackOverflowWebClient {
    Mono<StackOverflowQuestionResponse> fetchQuestion(String questionId);
}
