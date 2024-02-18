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

public class TrackCommandTest {
    @Mock
    private Update update;

    private TrackCommand trackCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        trackCommand = new TrackCommand();
    }

    @Test
    public void testCommand() {
        assertEquals("/track", trackCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("Track link.", trackCommand.description());
    }

    @Test
    public void testChatState() {
        assertEquals(ChatState.WAITING_FOR_COMMAND, trackCommand.chatState());
    }

    @Test
    public void testNextChatState() {
        assertEquals(ChatState.WAITING_FOR_LINK_TO_TRACK, trackCommand.nextChatState());
    }

    @Test
    public void testHandle() {
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);

        SendMessage sendMessage = trackCommand.handle(update);

        assertNotNull(sendMessage);
        assertEquals("Enter the URL, that you want to track.", sendMessage.getParameters().get("text"));
        assertEquals(12345L, sendMessage.getParameters().get("chat_id"));
    }
}
