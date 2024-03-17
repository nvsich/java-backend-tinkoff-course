package edu.java.scrapper.service.processor.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.exception.ChatIsRegisteredException;
import edu.java.scrapper.exception.ChatNotFoundException;
import edu.java.scrapper.repo.ChatRepo;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JdbcChatProcessorTest {
    private ChatRepo mockChatRepo;

    private JdbcChatProcessor chatProcessor;

    private static final Long CHAT_ID = 1L;

    @Captor
    ArgumentCaptor<Chat> chatArgumentCaptor;

    @Captor
    ArgumentCaptor<Long> longArgumentCaptor;

    @BeforeEach
    void setUp() {
        mockChatRepo = mock(ChatRepo.class);
        chatProcessor = new JdbcChatProcessor(mockChatRepo);
    }

    @Test
    void registerChat_shouldThrowException() {
        Chat mockChat = mock(Chat.class);
        when(mockChatRepo.findByChatId(CHAT_ID)).thenReturn(Optional.of(mockChat));

        assertThrows(ChatIsRegisteredException.class, () -> chatProcessor.registerChat(CHAT_ID));
    }

    @Test
    void registerChat_shouldSaveToChatRepo() {
        when(mockChatRepo.findByChatId(CHAT_ID)).thenReturn(Optional.empty());

        chatProcessor.registerChat(CHAT_ID);

        Mockito.verify(mockChatRepo, times(1))
            .save(chatArgumentCaptor.capture());

        Assertions.assertEquals(CHAT_ID, chatArgumentCaptor.getValue().getChatId());
    }

    @Test
    void deleteChat_shouldThrowException() {
        when(mockChatRepo.findByChatId(CHAT_ID)).thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> chatProcessor.deleteChat(CHAT_ID));
    }

    @Test
    void deleteChat_shouldDeleteFromChatRepo() {
        Chat mockChat = mock(Chat.class);
        when(mockChatRepo.findByChatId(CHAT_ID)).thenReturn(Optional.of(mockChat));

        chatProcessor.deleteChat(CHAT_ID);

        Mockito.verify(mockChatRepo, times(1))
            .deleteByChatId(longArgumentCaptor.capture());

        Assertions.assertEquals(CHAT_ID, longArgumentCaptor.getValue());
    }
}
