package edu.java.bot.service.command;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.Link;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.entity.enums.LinkDomain;
import edu.java.bot.entity.factory.LinkFactory;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.exception.UnsupportedDomainException;
import edu.java.bot.repo.ChatLinksRepo;
import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import java.net.ConnectException;
import java.net.URISyntaxException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TrackCommandTest {

    private final Long CHAT_ID = 1L;
    private final String URL = "URL";
    @Captor
    ArgumentCaptor<ChatState> chatStateArgumentCaptor;
    @Captor
    ArgumentCaptor<Link> linkArgumentCaptor;
    @Captor
    ArgumentCaptor<Long> longArgumentCaptor;
    @Mock
    private ChatStateRepo mockChatStateRepo;
    @Mock
    private LinkRepo mockLinkRepo;
    @Mock
    private ChatLinksRepo mockChatLinksRepo;
    @Mock
    private LinkFactory mockLinkFactory;
    @InjectMocks
    private TrackCommand trackCommand;
    @Mock
    private MessageRequest mockMessageRequest;

    @BeforeEach
    void setUp() {
        when(mockMessageRequest.getChatId()).thenReturn(CHAT_ID);
        when(mockMessageRequest.getText()).thenReturn(URL);
    }

    @Test
    @DisplayName("Throws ChatNotFoundException when user is not registered")
    void handle_ChatNotFoundException() {
        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> trackCommand.handle(mockMessageRequest));
    }

    @Test
    @DisplayName("Should change chatState and return message when initial ChatStatus is WAITING_FOR_COMMAND")
    void handle_TrackCommandFirstInteraction()
        throws ChatNotFoundException, UnsupportedDomainException, URISyntaxException, ConnectException {
        ChatState tChatState = new ChatState(CHAT_ID, ChatStatus.WAITING_FOR_COMMAND);

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        MessageResponse actualResponse = trackCommand.handle(mockMessageRequest);

        verify(mockChatStateRepo, times(1))
            .save(any(Long.class), chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(mockMessageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_LINK_TO_TRACK, actualChatState.getChatStatus());
        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
    }

    @Test
    @DisplayName("Should throw UnsupportedDomainException when created link is null and saving ChatState")
    void test_CreatedLinkIsNull() throws URISyntaxException, ConnectException {
        ChatState tChatState = new ChatState(CHAT_ID, ChatStatus.WAITING_FOR_LINK_TO_TRACK);

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        when(mockLinkFactory.createLink(mockMessageRequest.getText())).thenReturn(null);

        assertThrows(UnsupportedDomainException.class, () -> trackCommand.handle(mockMessageRequest));

        verify(mockChatStateRepo, times(1))
            .save(any(Long.class), chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(mockMessageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_COMMAND, actualChatState.getChatStatus());
    }

    @Test
    @DisplayName("Should throw UnsupportedDomainException when created link is unsupported and saving ChatState")
    void test_CreatedLinkIsNotSupported() throws URISyntaxException, ConnectException {
        ChatState tChatState = new ChatState(CHAT_ID, ChatStatus.WAITING_FOR_LINK_TO_TRACK);
        Link mockLink = mock(Link.class);

        when(mockLink.getLinkDomain()).thenReturn(LinkDomain.NOT_SUPPORTED);

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        when(mockLinkFactory.createLink(mockMessageRequest.getText())).thenReturn(mockLink);

        assertThrows(UnsupportedDomainException.class, () -> trackCommand.handle(mockMessageRequest));

        verify(mockChatStateRepo, times(1))
            .save(any(Long.class), chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(mockMessageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_COMMAND, actualChatState.getChatStatus());
    }

    @Test
    @DisplayName("Should save Link and ChatState, return message when link is correct")
    void test_LinkIsCorrect()
        throws URISyntaxException, ConnectException, ChatNotFoundException, UnsupportedDomainException {
        ChatState tChatState = new ChatState(CHAT_ID, ChatStatus.WAITING_FOR_LINK_TO_TRACK);
        Link mockLink = mock(Link.class);
        Long tLinkId = 123L;

        when(mockLink.getId()).thenReturn(tLinkId);
        when(mockLink.getLinkDomain()).thenReturn(LinkDomain.GitHub);

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        when(mockLinkFactory.createLink(mockMessageRequest.getText())).thenReturn(mockLink);

        MessageResponse actualResponse = trackCommand.handle(mockMessageRequest);

        verify(mockChatStateRepo, times(1))
            .save(any(Long.class), chatStateArgumentCaptor.capture());

        verify(mockLinkRepo, times(1))
            .save(linkArgumentCaptor.capture());

        verify(mockChatLinksRepo, times(1))
            .save(any(), longArgumentCaptor.capture());

        assertEquals(mockMessageRequest.getChatId(), chatStateArgumentCaptor.getValue().getChatId());
        assertEquals(ChatStatus.WAITING_FOR_COMMAND, chatStateArgumentCaptor.getValue().getChatStatus());

        assertEquals(mockLink.getId(), linkArgumentCaptor.getValue().getId());

        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
    }
}
