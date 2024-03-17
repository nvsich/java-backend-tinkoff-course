package edu.java.bot.service.processor.impl;

import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.exception.InvalidLinkUpdateException;
import edu.java.bot.service.processor.LinkUpdateProcessor;
import edu.java.bot.telegrambot.Bot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkUpdateProcessorImpl implements LinkUpdateProcessor {

    private Bot bot;

    @Override
    public void process(LinkUpdateRequest linkUpdateRequest) {
        if (linkUpdateRequest == null
            || linkUpdateRequest.getTgChatIds() == null
            || linkUpdateRequest.getTgChatIds().isEmpty()
            || linkUpdateRequest.getDescription().isBlank()) {
            throw new InvalidLinkUpdateException("Invalid LinkUpdateRequest");
        }

        for (var chatId : linkUpdateRequest.getTgChatIds()) {
            MessageResponse messageResponse = new MessageResponse(chatId, linkUpdateRequest.getDescription());
            bot.sendMessage(messageResponse);
        }
    }
}
