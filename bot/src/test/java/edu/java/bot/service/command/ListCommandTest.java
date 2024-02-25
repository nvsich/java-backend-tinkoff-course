package edu.java.bot.service.command;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.LinkDomain;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.repo.ChatLinksRepo;
import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ListCommandTest {
    private final Long CHAT_ID = 1L;
    @Mock
    private ChatLinksRepo mockChatLinksRepo;
    @Mock
    private ChatStateRepo mockChatStateRepo;
    @Mock
    private LinkRepo mockLinkRepo;
    @InjectMocks
    private ListCommand listCommand;
    private MessageRequest mockMessageRequest;

    @BeforeEach
    void setUp() {
        mockMessageRequest = mock(MessageRequest.class);
        when(mockMessageRequest.getChatId()).thenReturn(CHAT_ID);
    }

    @Test
    @DisplayName("Should throw ChatNotFoundException when chat not found")
    void handle_ThrowsException() {
        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> listCommand.handle(mockMessageRequest));
    }

    @Test
    @DisplayName("Should handle user not tracking links with special message")
    void handle_NoLinks_ReturnsNoLinksMessage() throws ChatNotFoundException {
        ChatState mockChatState = mock(ChatState.class);

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.ofNullable(mockChatState));

        when(mockChatLinksRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Collections.emptySet());

        MessageResponse actualResponse = listCommand.handle(mockMessageRequest);

        String expectedResponseText = "You are not tracking any links yet";

        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
        assertEquals(expectedResponseText, actualResponse.getText());
    }

    @Test
    @DisplayName("Should return user list of links")
    void handle_UserIsTrackingLinks_ReturnsListLinksMessage() throws ChatNotFoundException {
        ChatState mockChatState = mock(ChatState.class);

        Long linkId = 2L;
        LinkDomain tDomain = LinkDomain.GitHub;
        String tUrl = "tUrl";

        Link tLink = new Link();
        tLink.setId(linkId);
        tLink.setLinkDomain(tDomain);
        tLink.setUrl(tUrl);

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.ofNullable(mockChatState));

        when(mockChatLinksRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Set.of(linkId));

        when(mockLinkRepo.findAllById(Set.of(linkId))).thenReturn(List.of(tLink));

        MessageResponse actualResponse = listCommand.handle(mockMessageRequest);

        String expectedResponseText = "Current tracking links:\nid: 2. [" + tDomain + "] " + tUrl + '\n';

        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
        assertEquals(expectedResponseText, actualResponse.getText());
    }
}
