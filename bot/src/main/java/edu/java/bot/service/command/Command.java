package edu.java.bot.service.command;

import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;

public interface Command {

    String command();

    String description();

    MessageResponse handle(MessageRequest update);

    default boolean supports(MessageRequest request) {
        return request != null && request.getText() != null && request.getText().startsWith(command());
    }
}
