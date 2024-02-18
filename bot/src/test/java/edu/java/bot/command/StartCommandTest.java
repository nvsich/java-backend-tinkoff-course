package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.state.ChatState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StartCommandTest {
    @Mock
    private Update update;

    private StartCommand startCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        startCommand = new StartCommand();
    }

    @Test
    public void testCommand() {
        assertEquals("/start", startCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("Start bot", startCommand.description());
    }

    @Test
    public void testChatState() {
        assertEquals(ChatState.WAITING_FOR_START, startCommand.chatState());
    }

    @Test
    public void testNextChatState() {
        assertEquals(ChatState.WAITING_FOR_COMMAND, startCommand.nextChatState());
    }

    @Test
    public void testHandle() {
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);

        SendMessage sendMessage = startCommand.handle(update);

        assertNotNull(sendMessage);
        assertEquals("Welcome to Link Tracker!", sendMessage.getParameters().get("text"));
        assertEquals(12345L, sendMessage.getParameters().get("chat_id"));
    }
}

