package edu.java.scrapper.service.client;

import edu.java.scrapper.dto.response.StackOverflowQuestionResponse;

public interface StackOverflowWebClient {
    StackOverflowQuestionResponse fetchQuestion(String questionId);
}
