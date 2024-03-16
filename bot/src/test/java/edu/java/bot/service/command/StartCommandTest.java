package edu.java.bot.service.command;

import edu.java.bot.api.client.ScrapperClient;
import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.IncorrectRequestParamsException;
import edu.java.bot.repo.ChatStateRepo;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {
    private final Long CHAT_ID = 1L;
    @Captor
    ArgumentCaptor<ChatState> chatStateArgumentCaptor;
    private ChatStateRepo mockChatStateRepo;
    private ScrapperClient mockScrapperClient;
    private StartCommand startCommand;
    private MessageRequest mockMessageRequest;

    @BeforeEach
    void setUp() {
        mockScrapperClient = mock(ScrapperClient.class);
        mockChatStateRepo = mock(ChatStateRepo.class);
        mockMessageRequest = mock(MessageRequest.class);
        when(mockMessageRequest.getChatId()).thenReturn(CHAT_ID);

        startCommand = new StartCommand(mockChatStateRepo, mockScrapperClient);
    }

    @Test
    @DisplayName("Should save chatId in chatStateRepo and register in Scrapper client if first interaction")
    void handle_FirstInteraction_SaveToChatStateRepo() {
        when(mockChatStateRepo.findByChatId(mockMessageRequest.getChatId()))
            .thenReturn(Optional.empty());

        MessageResponse actualResponse = startCommand.handle(mockMessageRequest);

        String expectedResponseText = "Welcome to the Link Tracker Bot!";

        verify(mockChatStateRepo, times(1))
            .save(chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(mockMessageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_COMMAND, actualChatState.getChatStatus());
        assertEquals(expectedResponseText, actualResponse.getText());
        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
        verify(mockScrapperClient).registerChat(CHAT_ID);
    }

    @Test
    @DisplayName("Should return message if user is not first timer")
    void handle_NotFirstInteraction_ReturnMessage() {
        ChatState mockChatState = mock(ChatState.class);

        when(mockChatStateRepo.findByChatId(mockMessageRequest.getChatId()))
            .thenReturn(Optional.ofNullable(mockChatState));

        MessageResponse actualResponse = startCommand.handle(mockMessageRequest);

        String expectedResponseText = "Bot is already started";

        assertEquals(expectedResponseText, actualResponse.getText());
        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
    }

    @Test
    @DisplayName("Should send message when chat registration fails")
    void handle_ChatRegistrationFails_SendsErrorMessage() {
        when(mockChatStateRepo.findByChatId(CHAT_ID))
            .thenReturn(Optional.empty());

        when(mockScrapperClient.registerChat(CHAT_ID))
            .thenThrow(new IncorrectRequestParamsException("Invalid parameters"));

        MessageResponse response = startCommand.handle(mockMessageRequest);

        assertEquals("Invalid parameters", response.getText());
    }
}
