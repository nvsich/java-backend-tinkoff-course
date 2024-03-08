package edu.java.bot.service.command;

import edu.java.bot.api.client.ScrapperClient;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListLinkResponse;
import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.exception.IncorrectRequestParamsException;
import edu.java.bot.repo.ChatStateRepo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListCommandTest {
    private final Long CHAT_ID = 1L;
    private ChatStateRepo mockChatStateRepo;
    private ListCommand listCommand;
    private MessageRequest mockMessageRequest;
    private ScrapperClient mockScrapperClient;

    @BeforeEach
    void setUp() {
        mockChatStateRepo = mock(ChatStateRepo.class);
        mockScrapperClient = mock(ScrapperClient.class);
        mockMessageRequest = mock(MessageRequest.class);

        listCommand = new ListCommand(mockChatStateRepo, mockScrapperClient);

        when(mockMessageRequest.getChatId()).thenReturn(CHAT_ID);
    }

    @Test
    @DisplayName("Should throw ChatNotFoundException when chat not found")
    void handle_ThrowsException() {
        when(mockChatStateRepo.findByChatId(mockMessageRequest.getChatId()))
            .thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> listCommand.handle(mockMessageRequest));
    }

    @Test
    @DisplayName("Should handle user not tracking links with special message")
    void handle_NoLinks_ReturnsNoLinksMessage() {
        ChatState mockChatState = mock(ChatState.class);

        when(mockChatStateRepo.findByChatId(mockMessageRequest.getChatId()))
            .thenReturn(Optional.ofNullable(mockChatState));

        when(mockScrapperClient.getAllLinksForChat(CHAT_ID)).thenReturn(Mono.empty());

        MessageResponse actualResponse = listCommand.handle(mockMessageRequest);

        String expectedResponseText = "You are not tracking any links yet";

        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
        assertEquals(expectedResponseText, actualResponse.getText());
    }

    @Test
    @DisplayName("Should return exception message when incorrect request params")
    void handle_IncorrectRequestParams() {
        ChatState mockChatState = mock(ChatState.class);

        when(mockChatStateRepo.findByChatId(mockMessageRequest.getChatId()))
            .thenReturn(Optional.ofNullable(mockChatState));

        String expectedResponseText = "Exception params";

        when(mockScrapperClient.getAllLinksForChat(CHAT_ID))
            .thenThrow(new IncorrectRequestParamsException(expectedResponseText));

        MessageResponse actualResponse = listCommand.handle(mockMessageRequest);

        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
        assertEquals(expectedResponseText, actualResponse.getText());
    }

    @Test
    @DisplayName("Should return user list of links")
    void handle_UserIsTrackingLinks_ReturnsListLinksMessage() throws URISyntaxException {
        ChatState mockChatState = mock(ChatState.class);

        when(mockChatStateRepo.findByChatId(mockMessageRequest.getChatId()))
            .thenReturn(Optional.ofNullable(mockChatState));

        URI tUri = new URI("https://example.com/");
        var linkResponse = new LinkResponse();
        linkResponse.setId(1L);
        linkResponse.setUrl(tUri);
        var listLinkResponse = new ListLinkResponse();
        listLinkResponse.setLinks(List.of(linkResponse));

        when(mockScrapperClient.getAllLinksForChat(CHAT_ID)).thenReturn(Mono.just(listLinkResponse));

        MessageResponse actualResponse = listCommand.handle(mockMessageRequest);

        String expectedTest = "Current tracking links:\n" + tUri.toString() + '\n';

        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
        assertEquals(expectedTest, actualResponse.getText());
    }
}
