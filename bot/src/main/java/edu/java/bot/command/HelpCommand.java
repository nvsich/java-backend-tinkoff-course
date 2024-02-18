package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.state.ChatState;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HelpCommand implements Command {

    private ApplicationContext applicationContext;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "View commands list";
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
        StringBuilder helpMessage = new StringBuilder();

        var beans = applicationContext.getBeansOfType(Command.class);

        for (Command command : beans.values()) {
            helpMessage
                .append(command.command())
                .append(": ")
                .append(command.description())
                .append("\n");
        }

        return new SendMessage(update.message().chat().id(), helpMessage.toString());
    }
}
