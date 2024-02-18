package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repo.LinkRepo;
import edu.java.bot.state.ChatState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListCommand implements Command {
    private LinkRepo linkRepo;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "View current tracking links list";
    }

    @Override
    public ChatState chatState() {
        return ChatState.WAITING_FOR_COMMAND;
    }

    @Override
    public ChatState nextChatState() {
        return ChatState.WAITING_FOR_COMMAND;
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder listMessage = new StringBuilder();
        Long chatId = update.message().chat().id();

        int counter = 0;

        var userLinksList = linkRepo.getAll(chatId);

        while (userLinksList != null && counter < userLinksList.size()) {
            counter++;
            listMessage
                .append(counter)
                .append(". ")
                .append(userLinksList.get(counter - 1));
        }

        String replyMessage =
            (counter == 0 ? "You haven't tracked any links yet." : listMessage.toString());

        return new SendMessage(update.message().chat().id(), replyMessage);
    }
}
