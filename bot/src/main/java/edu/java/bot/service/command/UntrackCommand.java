package edu.java.bot.service.command;

import edu.java.bot.api.client.ScrapperClient;
import edu.java.bot.dto.request.DeleteLinkRequest;
import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.exception.IncorrectRequestParamsException;
import edu.java.bot.exception.LinkNotFoundException;
import edu.java.bot.repo.ChatStateRepo;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UntrackCommand implements Command {

    private final ChatStateRepo chatStateRepo;

    private ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stop tracking link";
    }

    @Override
    @Transactional
    public MessageResponse handle(MessageRequest request) {
        Long chatId = request.getChatId();

        ChatState chatState = chatStateRepo.findByChatId(chatId).orElseThrow(ChatNotFoundException::new);

        if (chatState.getChatStatus().equals(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK)) {
            chatStateRepo.save(
                ChatState
                    .builder()
                    .chatId(chatId)
                    .chatStatus(ChatStatus.WAITING_FOR_COMMAND)
                    .build()
            );

            try {
                var removeLinkRequest = new DeleteLinkRequest(request.getText());
                var linkResponse = scrapperClient.deleteLinkForChat(chatId, removeLinkRequest);

                return new MessageResponse(
                    chatId,
                    "Link " + linkResponse.getUrl().toString() + " removed from your tracking list"
                );

            } catch (IncorrectRequestParamsException | LinkNotFoundException e) {
                return new MessageResponse(chatId, e.getMessage());
            }
        }

        chatStateRepo.save(
            ChatState
                .builder()
                .chatId(chatId)
                .chatStatus(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK)
                .build()
        );
        return new MessageResponse(chatId, "Enter the id of link, that you don't want to track anymore");
    }

    @Override
    public boolean supports(MessageRequest request) {
        Long chatId = request.getChatId();
        Optional<ChatState> chatState = chatStateRepo.findByChatId(chatId);

        boolean isWaitingForLink =
            chatState.isPresent() && chatState.get().getChatStatus().equals(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK);

        return Command.super.supports(request) || isWaitingForLink;
    }
}
