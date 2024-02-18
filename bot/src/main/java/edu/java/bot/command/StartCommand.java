package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.state.ChatState;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Start bot";
    }

    @Override
    public ChatState chatState() {
        return ChatState.WAITING_FOR_START;
    }

    @Override
    public ChatState nextChatState() {
        return ChatState.WAITING_FOR_COMMAND;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Welcome to Link Tracker!");
    }
}
