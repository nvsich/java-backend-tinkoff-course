package edu.java.bot.service.command;

import edu.java.bot.api.client.ScrapperClient;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.repo.ChatStateRepo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackCommandTest {
    private final Long CHAT_ID = 1L;
    private final String URL = "https://example.com";
    @Captor
    ArgumentCaptor<ChatState> chatStateArgumentCaptor;
    @Captor
    ArgumentCaptor<Long> longArgumentCaptor;
    private ChatStateRepo mockChatStateRepo;
    private TrackCommand trackCommand;
    private MessageRequest messageRequest;
    private ScrapperClient mockScrapperClient;

    @BeforeEach
    void setUp() {
        mockScrapperClient = mock(ScrapperClient.class);
        mockChatStateRepo = mock(ChatStateRepo.class);

        messageRequest = new MessageRequest(CHAT_ID, URL);

        trackCommand = new TrackCommand(mockChatStateRepo, mockScrapperClient);
    }

    @Test
    @DisplayName("Throws ChatNotFoundException when user is not registered")
    void handle_ChatNotFoundException() {
        when(mockChatStateRepo.findByChatId(messageRequest.getChatId()))
            .thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> trackCommand.handle(messageRequest));
    }

    @Test
    @DisplayName("Should change chatState and return message when initial ChatStatus is WAITING_FOR_COMMAND")
    void handle_TrackCommandFirstInteraction() throws ChatNotFoundException {
        ChatState tChatState = ChatState.builder()
            .chatId(CHAT_ID)
            .chatStatus(ChatStatus.WAITING_FOR_COMMAND)
            .build();

        when(mockChatStateRepo.findByChatId(messageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        MessageResponse actualResponse = trackCommand.handle(messageRequest);

        verify(mockChatStateRepo, times(1))
            .save(chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(messageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_LINK_TO_TRACK, actualChatState.getChatStatus());
        assertEquals(messageRequest.getChatId(), actualResponse.getChatId());
    }

    @Test
    @DisplayName("Should return link with message when successfully added")
    void handle_SuccessfullyAddedLink() throws URISyntaxException {
        ChatState tChatState = ChatState.builder()
            .chatId(CHAT_ID)
            .chatStatus(ChatStatus.WAITING_FOR_LINK_TO_TRACK)
            .build();

        when(mockChatStateRepo.findByChatId(messageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        var tAddLinkRequest = new AddLinkRequest(URL);
        var tLinkResponse = new LinkResponse();
        URI tUri = new URI(URL);
        tLinkResponse.setUrl(tUri);
        when(mockScrapperClient.addLinkToChat(CHAT_ID, tAddLinkRequest)).thenReturn(tLinkResponse);

        MessageResponse actualResponse = trackCommand.handle(messageRequest);

        verify(mockChatStateRepo, times(1))
            .save(chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(messageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_COMMAND, actualChatState.getChatStatus());
        assertEquals(messageRequest.getChatId(), actualResponse.getChatId());
        assertEquals("Link " + tUri + " added to your tracking list", actualResponse.getText());
    }

    @Test
    @DisplayName("Should return message when exception from ScrapperClient")
    void handle_ExceptionFromScrapperClient() {
        ChatState tChatState = ChatState.builder()
            .chatId(CHAT_ID)
            .chatStatus(ChatStatus.WAITING_FOR_LINK_TO_TRACK)
            .build();

        when(mockChatStateRepo.findByChatId(messageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        String tExceptionMessage = "exception";

        when(mockScrapperClient.addLinkToChat(any(), any()))
            .thenThrow(new ChatNotFoundException(tExceptionMessage));

        MessageResponse actualResponse = trackCommand.handle(messageRequest);

        assertEquals(messageRequest.getChatId(), actualResponse.getChatId());
        assertEquals(tExceptionMessage, actualResponse.getText());
    }
}
