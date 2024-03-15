package edu.java.bot.service.command;

import edu.java.bot.api.client.ScrapperClient;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.exception.IncorrectRequestParamsException;
import edu.java.bot.repo.ChatStateRepo;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TrackCommand implements Command {

    private final ChatStateRepo chatStateRepo;

    private ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Start tracking link";
    }

    @Override
    @Transactional
    public MessageResponse handle(MessageRequest request) {
        Long chatId = request.getChatId();

        ChatState chatState = chatStateRepo.findByChatId(chatId).orElseThrow(ChatNotFoundException::new);

        if (chatState.getChatStatus().equals(ChatStatus.WAITING_FOR_LINK_TO_TRACK)) {
            chatStateRepo.save(
                ChatState
                    .builder()
                    .chatId(chatId)
                    .chatStatus(ChatStatus.WAITING_FOR_COMMAND)
                    .build()
            );

            try {
                var addLinkRequest = new AddLinkRequest(request.getText());
                var linkResponse = scrapperClient.addLinkToChat(chatId, addLinkRequest);

                return new MessageResponse(
                    chatId,
                    "Link " + linkResponse.getUrl().toString() + " added to your tracking list"
                );

            } catch (IncorrectRequestParamsException | ChatNotFoundException e) {
                return new MessageResponse(chatId, e.getMessage());
            }
        }
        chatStateRepo.save(
            ChatState
                .builder()
                .chatId(chatId)
                .chatStatus(ChatStatus.WAITING_FOR_LINK_TO_TRACK)
                .build()
        );
        return new MessageResponse(chatId, "Enter the link, that you want to track");
    }

    @Override
    public boolean supports(MessageRequest request) {
        Long chatId = request.getChatId();
        Optional<ChatState> chatState = chatStateRepo.findByChatId(chatId);

        boolean isWaitingForLink =
            chatState.isPresent() && chatState.get().getChatStatus().equals(ChatStatus.WAITING_FOR_LINK_TO_TRACK);

        return Command.super.supports(request) || isWaitingForLink;
    }
}
