package edu.java.bot.telegrambot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.entity.MessageResponse;
import java.util.List;

public interface Bot extends UpdatesListener, AutoCloseable {

    @Override
    int process(List<Update> updates);

    void start();

    @Override
    void close();

    void sendMessage(MessageResponse messageResponse);

}
