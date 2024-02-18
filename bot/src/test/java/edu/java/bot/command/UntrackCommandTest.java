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

public class UntrackCommandTest {
    @Mock
    private Update update;

    private UntrackCommand untrackCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        untrackCommand = new UntrackCommand();
    }

    @Test
    public void testCommand() {
        assertEquals("/untrack", untrackCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("Stop tracking link.", untrackCommand.description());
    }

    @Test
    public void testChatState() {
        assertEquals(ChatState.WAITING_FOR_COMMAND, untrackCommand.chatState());
    }

    @Test
    public void testNextChatState() {
        assertEquals(ChatState.WAITING_FOR_LINK_TO_UNTRACK, untrackCommand.nextChatState());
    }

    @Test
    public void testHandle() {
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);

        SendMessage sendMessage = untrackCommand.handle(update);

        assertNotNull(sendMessage);
        assertEquals("Enter the URL, that you don't want to track anymore.", sendMessage.getParameters().get("text"));
        assertEquals(12345L, sendMessage.getParameters().get("chat_id"));
    }
}
