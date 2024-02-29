package edu.java.bot.service.command;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.repo.ChatStateRepo;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class StartCommandTest {
    private final Long CHAT_ID = 1L;
    @Captor
    ArgumentCaptor<ChatState> chatStateArgumentCaptor;
    @Mock
    private ChatStateRepo mockChatStateRepo;
    @InjectMocks
    private StartCommand startCommand;
    private MessageRequest mockMessageRequest;

    @BeforeEach
    void setUp() {
        mockMessageRequest = mock(MessageRequest.class);
        when(mockMessageRequest.getChatId()).thenReturn(CHAT_ID);
    }

    @Test
    @DisplayName("Should save chatId in chatStateRepo if first interaction")
    void handle_FirstInteraction_SaveToChatStateRepo() {
        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.empty());

        MessageResponse actualResponse = startCommand.handle(mockMessageRequest);

        String expectedResponseText = "Welcome to the Link Tracker Bot!";

        verify(mockChatStateRepo, times(1))
            .save(any(Long.class), chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(mockMessageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_COMMAND, actualChatState.getChatStatus());
        assertEquals(expectedResponseText, actualResponse.getText());
        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
    }

    @Test
    @DisplayName("Should return message if user is not first timer")
    void handle_NotFirstInteraction_ReturnMessage() {
        ChatState mockChatState = mock(ChatState.class);

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.ofNullable(mockChatState));

        MessageResponse actualResponse = startCommand.handle(mockMessageRequest);

        String expectedResponseText = "Bot is already started";

        assertEquals(expectedResponseText, actualResponse.getText());
        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
    }

}
