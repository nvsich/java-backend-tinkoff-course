package edu.java.bot.service.command;

import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        MessageRequest mockRequest = mock(MessageRequest.class);

        Long chatId = 1L;
        when(mockRequest.getChatId()).thenReturn(chatId);

        MessageResponse actualResponse = helpCommand.handle(mockRequest);

        assertEquals(chatId, actualResponse.getChatId());
    }
}
