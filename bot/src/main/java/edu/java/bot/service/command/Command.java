package edu.java.bot.service.command;

import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.exception.UnsupportedDomainException;
import java.net.ConnectException;
import java.net.URISyntaxException;

public interface Command {
    String command();

    String description();

    MessageResponse handle(MessageRequest update)
        throws ChatNotFoundException, URISyntaxException, ConnectException, UnsupportedDomainException,
        IllegalArgumentException;

    default boolean supports(MessageRequest request) {
        return request != null && request.getText() != null && request.getText().startsWith(command());
    }
}
