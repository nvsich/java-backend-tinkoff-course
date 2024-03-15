package edu.java.bot.service.command;

import edu.java.bot.api.client.ScrapperClient;
import edu.java.bot.dto.request.DeleteLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.exception.LinkNotFoundException;
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
class UntrackCommandTest {
    private final Long CHAT_ID = 1L;
    private final String MESSAGE = "https://example.com";
    @Captor
    ArgumentCaptor<ChatState> chatStateArgumentCaptor;
    private ChatStateRepo mockChatStateRepo;
    private UntrackCommand untrackCommand;
    private MessageRequest messageRequest;
    private ScrapperClient mockScrapperClient;

    @BeforeEach
    void setUp() {
        mockChatStateRepo = mock(ChatStateRepo.class);
        mockScrapperClient = mock(ScrapperClient.class);
        messageRequest = new MessageRequest(CHAT_ID, MESSAGE);
        untrackCommand = new UntrackCommand(mockChatStateRepo, mockScrapperClient);
    }

    @Test
    @DisplayName("Throws ChatNotFoundException when user is not registered")
    void handle_ChatNotFoundException() {
        when(mockChatStateRepo.findByChatId(messageRequest.getChatId()))
            .thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> untrackCommand.handle(messageRequest));
    }

    @Test
    @DisplayName("Should change chatState and return message when initial ChatStatus is WAITING_FOR_COMMAND")
    void handle_UntrackCommandFirstInteraction() throws ChatNotFoundException {
        ChatState tChatState = ChatState.builder()
            .chatId(CHAT_ID)
            .chatStatus(ChatStatus.WAITING_FOR_COMMAND)
            .build();

        when(mockChatStateRepo.findByChatId(messageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        MessageResponse actualResponse = untrackCommand.handle(messageRequest);

        verify(mockChatStateRepo, times(1))
            .save(chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(messageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK, actualChatState.getChatStatus());
        assertEquals(messageRequest.getChatId(), actualResponse.getChatId());
    }

    @Test
    @DisplayName("Should return link with message when successfully removed")
    void handle_SuccessfullyAddedLink() throws URISyntaxException {
        ChatState tChatState = ChatState.builder()
            .chatId(CHAT_ID)
            .chatStatus(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK)
            .build();

        when(mockChatStateRepo.findByChatId(messageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        var tRemoveLinkRequest = new DeleteLinkRequest(MESSAGE);
        var tLinkResponse = new LinkResponse();
        URI tUri = new URI(MESSAGE);
        tLinkResponse.setUrl(tUri);
        when(mockScrapperClient.deleteLinkForChat(CHAT_ID, tRemoveLinkRequest)).thenReturn(Mono.just(tLinkResponse));

        MessageResponse actualResponse = untrackCommand.handle(messageRequest);

        verify(mockChatStateRepo, times(1))
            .save(chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(messageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_COMMAND, actualChatState.getChatStatus());
        assertEquals(messageRequest.getChatId(), actualResponse.getChatId());
        assertEquals("Link " + MESSAGE + " removed from your tracking list", actualResponse.getText());
    }

    @Test
    @DisplayName("Should return message when exception from ScrapperClient")
    void handle_ExceptionFromScrapperClient() {
        ChatState tChatState = ChatState.builder()
            .chatId(CHAT_ID)
            .chatStatus(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK)
            .build();

        when(mockChatStateRepo.findByChatId(messageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        String tExceptionMessage = "exception";

        when(mockScrapperClient.deleteLinkForChat(any(), any()))
            .thenThrow(new LinkNotFoundException(tExceptionMessage));

        MessageResponse actualResponse = untrackCommand.handle(messageRequest);

        assertEquals(messageRequest.getChatId(), actualResponse.getChatId());
        assertEquals(tExceptionMessage, actualResponse.getText());
    }
}
