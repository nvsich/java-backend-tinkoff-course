package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.processor.UserMessageProcessor;
import jakarta.annotation.PostConstruct;
import java.util.List;

public class BotImpl implements Bot {
    private final TelegramBot telegramBot;
    private final UserMessageProcessor userMessageProcessor;

    public BotImpl(
        TelegramBot telegramBot, UserMessageProcessor userMessageProcessor
    ) {
        this.telegramBot = telegramBot;
        this.userMessageProcessor = userMessageProcessor;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            SendMessage response = userMessageProcessor.process(update);
            if (response != null) {
                execute(response);
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    @PostConstruct
    public void start() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public void close() {
        telegramBot.removeGetUpdatesListener();
    }
}
