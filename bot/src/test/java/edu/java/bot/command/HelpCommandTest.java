package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.state.ChatState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

public class HelpCommandTest {
    @MockitoSettings
    private ApplicationContext applicationContext;
    @Mock
    private Update update;

    private HelpCommand helpCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        helpCommand = new HelpCommand(applicationContext);
    }

    @Test
    public void testCommand() {
        assertEquals("/help", helpCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("View commands list", helpCommand.description());
    }

    @Test
    public void testChatState() {
        assertEquals(ChatState.WAITING_FOR_COMMAND, helpCommand.chatState());
    }

    @Test
    public void testNextChatState() {
        assertEquals(ChatState.WAITING_FOR_COMMAND, helpCommand.nextChatState());
    }
}
