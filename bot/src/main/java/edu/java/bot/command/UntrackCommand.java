package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.state.ChatState;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stop tracking link.";
    }

    @Override
    public ChatState chatState() {
        return ChatState.WAITING_FOR_COMMAND;
    }

    @Override
    public ChatState nextChatState() {
        return ChatState.WAITING_FOR_LINK_TO_UNTRACK;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(
            update.message().chat().id(),
            "Enter the URL, that you don't want to track anymore."
        );
    }
}
