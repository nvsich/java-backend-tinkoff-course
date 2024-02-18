package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.processor.UserMessageProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotImplTest {
    @Mock
    private TelegramBot telegramBot;

    @Mock
    private UserMessageProcessor userMessageProcessor;

    private BotImpl botImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        botImpl = new BotImpl(telegramBot, userMessageProcessor);
    }

    @Test
    void execute_CallsTelegramBotExecute() {
        BaseRequest request = mock(BaseRequest.class);
        botImpl.execute(request);
        verify(telegramBot, times(1)).execute(request);
    }

    @Test
    void process_ProcessesUpdatesAndSendsMessages() {
        Update update = mock(Update.class);
        SendMessage response = mock(SendMessage.class);
        when(userMessageProcessor.process(update)).thenReturn(response);

        botImpl.process(Collections.singletonList(update));

        verify(userMessageProcessor, times(1)).process(update);
        verify(telegramBot, times(1)).execute(response);
    }

    @Test
    void process_ReturnsConfirmedUpdatesAll() {
        Update update = mock(Update.class);
        when(userMessageProcessor.process(update)).thenReturn(null);

        int result = botImpl.process(Collections.singletonList(update));

        assert result == UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Test
    void start_SetsUpdatesListener() {
        botImpl.start();
        verify(telegramBot, times(1))
            .setUpdatesListener(ArgumentMatchers.any(UpdatesListener.class));
    }

    @Test
    void close_RemovesGetUpdatesListener() {
        botImpl.close();
        verify(telegramBot, times(1)).removeGetUpdatesListener();
    }
}
