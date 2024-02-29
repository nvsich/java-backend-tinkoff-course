package edu.java.bot.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.service.command.Command;
import edu.java.bot.service.processor.UserMessageProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BotImpl implements Bot {

    private final TelegramBot telegramBot;

    private final UserMessageProcessor userMessageProcessor;

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update != null && update.message() != null) {
                MessageRequest request = new MessageRequest(update.message().chat().id(), update.message().text());
                MessageResponse response = userMessageProcessor.process(request);

                telegramBot.execute(new SendMessage(response.getChatId(), response.getText()));
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    @PostConstruct
    public void start() {
        List<? extends Command> commands = userMessageProcessor.commands();
        BotCommand[] commandsArray = new BotCommand[commands.size()];
        for (int i = 0; i < commands.size(); i++) {
            commandsArray[i] = toApiCommand(commands.get(i));
        }

        execute(new SetMyCommands(commandsArray));

        this.telegramBot.setUpdatesListener(this);
    }

    @Override
    @PreDestroy
    public void close() {
        this.telegramBot.removeGetUpdatesListener();
    }

    private <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        this.telegramBot.execute(request);
    }

    private BotCommand toApiCommand(Command command) {
        return new BotCommand(command.command(), command.description());
    }
}
