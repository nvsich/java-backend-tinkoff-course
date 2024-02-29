package edu.java.bot.service.command;

import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelpCommandTest {
    @Mock
    private Command mockCommand1;

    @Mock
    private Command mockCommand2;

    private HelpCommand helpCommand;

    @BeforeEach
    void setUp() {
        helpCommand = new HelpCommand(Arrays.asList(mockCommand1, mockCommand2));
    }

    @Test
    @DisplayName("Should send message to the same chat")
    void handleTest() {
        Long chatId = 1L;
        String helpResponse = "";
        MessageRequest mockRequest = new MessageRequest(chatId, helpResponse);

        MessageResponse actualResponse = helpCommand.handle(mockRequest);

        assertEquals(chatId, actualResponse.getChatId());
    }
}
