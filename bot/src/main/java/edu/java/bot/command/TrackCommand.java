package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.state.ChatState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrackCommand implements Command {

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Track link.";
    }

    @Override
    public ChatState chatState() {
        return ChatState.WAITING_FOR_COMMAND;
    }

    @Override
    public ChatState nextChatState() {
        return ChatState.WAITING_FOR_LINK_TO_TRACK;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(
            update.message().chat().id(),
            "Enter the URL, that you want to track."
        );
    }
}
