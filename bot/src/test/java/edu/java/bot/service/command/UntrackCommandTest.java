package edu.java.bot.service.command;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.MessageRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.exception.ChatNotFoundException;
import edu.java.bot.repo.ChatLinksRepo;
import edu.java.bot.repo.ChatStateRepo;
import edu.java.bot.repo.LinkRepo;
import java.util.Optional;
import java.util.Set;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UntrackCommandTest {
    private final Long CHAT_ID = 1L;
    @Captor
    ArgumentCaptor<ChatState> chatStateArgumentCaptor;
    @Captor
    ArgumentCaptor<Long> longArgumentCaptor;
    @Mock
    private ChatStateRepo mockChatStateRepo;
    @Mock
    private ChatLinksRepo mockChatLinksRepo;
    @Mock
    private LinkRepo mockLinkRepo;
    @InjectMocks
    private UntrackCommand untrackCommand;
    @Mock
    private MessageRequest mockMessageRequest;

    @BeforeEach
    void setUp() {
        when(mockMessageRequest.getChatId()).thenReturn(CHAT_ID);
    }

    @Test
    @DisplayName("Throws ChatNotFoundException when user is not registered")
    void handle_ChatNotFoundException() {
        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> untrackCommand.handle(mockMessageRequest));
    }

    @Test
    @DisplayName("Should change chatState and return message when initial ChatStatus is WAITING_FOR_COMMAND")
    void handle_UntrackCommandFirstInteraction() throws ChatNotFoundException {
        ChatState tChatState = new ChatState(CHAT_ID, ChatStatus.WAITING_FOR_COMMAND);

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        MessageResponse actualResponse = untrackCommand.handle(mockMessageRequest);

        verify(mockChatStateRepo, times(1))
            .save(any(Long.class), chatStateArgumentCaptor.capture());

        ChatState actualChatState = chatStateArgumentCaptor.getValue();

        assertEquals(mockMessageRequest.getChatId(), actualChatState.getChatId());
        assertEquals(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK, actualChatState.getChatStatus());
        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when given id is not correct")
    void handle_GivenIdIsNotCorrect_ThrowsIllegalArgumentException() {
        ChatState tChatState = new ChatState(CHAT_ID, ChatStatus.WAITING_FOR_LINK_TO_UNTRACK);
        String tIncorrectId = "URL";

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        when(mockMessageRequest.getText()).thenReturn(tIncorrectId);

        assertThrows(IllegalArgumentException.class, () -> untrackCommand.handle(mockMessageRequest));

        verify(mockChatStateRepo, times(1))
            .save(any(Long.class), chatStateArgumentCaptor.capture());

        assertEquals(ChatStatus.WAITING_FOR_COMMAND, chatStateArgumentCaptor.getValue().getChatStatus());
        assertEquals(mockMessageRequest.getChatId(), chatStateArgumentCaptor.getValue().getChatId());
    }

    @Test
    @DisplayName("Should return special message when user is not tracking ling with given correct id")
    void handle_CorrectIdNotFound() throws ChatNotFoundException {
        ChatState tChatState = new ChatState(CHAT_ID, ChatStatus.WAITING_FOR_LINK_TO_UNTRACK);
        String tLinkId = "123";

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        when(mockMessageRequest.getText()).thenReturn(tLinkId);

        when(mockChatLinksRepo.findById(mockMessageRequest.getChatId())).thenReturn(Set.of());

        MessageResponse actualResponse = untrackCommand.handle(mockMessageRequest);

        verify(mockChatStateRepo, times(1))
            .save(any(Long.class), chatStateArgumentCaptor.capture());

        assertEquals(ChatStatus.WAITING_FOR_COMMAND, chatStateArgumentCaptor.getValue().getChatStatus());
        assertEquals(mockMessageRequest.getChatId(), chatStateArgumentCaptor.getValue().getChatId());

        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
    }

    @Test
    @DisplayName("Should delete link with given correct id")
    void handle_CorrectIdFound_DeleteFromRepo() throws ChatNotFoundException {
        ChatState tChatState = new ChatState(CHAT_ID, ChatStatus.WAITING_FOR_LINK_TO_UNTRACK);
        String tLinkId = "123";
        Long tLinkIdParsed = Long.parseLong(tLinkId);

        when(mockChatStateRepo.findById(mockMessageRequest.getChatId()))
            .thenReturn(Optional.of(tChatState));

        when(mockMessageRequest.getText()).thenReturn(tLinkId);

        when(mockChatLinksRepo.findById(mockMessageRequest.getChatId())).thenReturn(Set.of(tLinkIdParsed));

        MessageResponse actualResponse = untrackCommand.handle(mockMessageRequest);

        verify(mockChatStateRepo, times(1))
            .save(any(Long.class), chatStateArgumentCaptor.capture());

        verify(mockChatLinksRepo, times(1))
            .deleteLinkId(any(Long.class), longArgumentCaptor.capture());

        assertEquals(ChatStatus.WAITING_FOR_COMMAND, chatStateArgumentCaptor.getValue().getChatStatus());
        assertEquals(mockMessageRequest.getChatId(), chatStateArgumentCaptor.getValue().getChatId());

        assertEquals(tLinkIdParsed, longArgumentCaptor.getValue());

        assertEquals(mockMessageRequest.getChatId(), actualResponse.getChatId());
    }
}
