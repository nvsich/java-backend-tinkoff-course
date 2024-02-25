package edu.java.bot.service.command;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.repo.ChatStateRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StartCommand implements Command {

    private final ChatStateRepo chatStateRepo;

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

        if (chatStateRepo.findById(chatId).isEmpty()) {
            chatStateRepo.save(chatId, new ChatState(chatId, ChatStatus.WAITING_FOR_COMMAND));
            return new MessageResponse(chatId, "Welcome to the Link Tracker Bot!");
        }

        return new MessageResponse(chatId, "Bot is already started");
    }
}
