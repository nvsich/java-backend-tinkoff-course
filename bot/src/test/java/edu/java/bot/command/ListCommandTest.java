package edu.java.bot.command;

import edu.java.bot.repo.LinkRepo;
import edu.java.bot.state.ChatState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;

public class ListCommandTest {

    @Mock
    private LinkRepo linkRepo;
    private ListCommand listCommand;

    @BeforeEach
    public void setUp() {
        listCommand = new ListCommand(linkRepo);
    }

    @Test
    public void testCommand() {
        assertEquals("/list", listCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("View current tracking links list", listCommand.description());
    }

    @Test
    public void testChatState() {
        assertEquals(ChatState.WAITING_FOR_COMMAND, listCommand.chatState());
    }

    @Test
    public void testNextChatState() {
        assertEquals(ChatState.WAITING_FOR_COMMAND, listCommand.nextChatState());
    }
}
