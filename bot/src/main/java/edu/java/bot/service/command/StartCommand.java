package edu.java.bot.service.command;

import edu.java.bot.api.client.ScrapperClient;
import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.IncorrectRequestParamsException;
import edu.java.bot.repo.ChatStateRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StartCommand implements Command {

    private final ChatStateRepo chatStateRepo;

    private ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Start bot";
    }

    @Override
    public MessageResponse handle(MessageRequest request) {
        Long chatId = request.getChatId();

        if (chatStateRepo.findByChatId(chatId).isEmpty()) {
            chatStateRepo.save(
                ChatState
                    .builder()
                    .chatId(chatId)
                    .chatStatus(ChatStatus.WAITING_FOR_COMMAND)
                    .build()
            );

            try {
                scrapperClient.registerChat(chatId).block();
            } catch (IncorrectRequestParamsException e) {
                return new MessageResponse(chatId, e.getMessage());
            }

            return new MessageResponse(chatId, "Welcome to the Link Tracker Bot!");
        }

        return new MessageResponse(chatId, "Bot is already started");
    }
}
