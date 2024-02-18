package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.state.ChatState;

public interface Command {

    String command();

    String description();

    ChatState chatState();

    ChatState nextChatState();

    SendMessage handle(Update update);

    default boolean supports(Update update, ChatState chatState) {
        return update.message() != null
            && update.message().text().startsWith(command())
            && chatState().equals(chatState);
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
