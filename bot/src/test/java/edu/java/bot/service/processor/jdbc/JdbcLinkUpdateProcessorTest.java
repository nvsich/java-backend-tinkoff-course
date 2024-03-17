package edu.java.bot.service.processor.jdbc;

import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.entity.MessageResponse;
import edu.java.bot.exception.InvalidLinkUpdateException;
import edu.java.bot.telegrambot.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class JdbcLinkUpdateProcessorTest {

    @Mock
    private Bot bot;

    @InjectMocks
    private JdbcLinkUpdateProcessor linkUpdateProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcess_ValidRequest() {
        LinkUpdateRequest request = new LinkUpdateRequest();
        var listIds = List.of(123L, 456L);
        request.setTgChatIds(listIds);
        request.setDescription("Test description");

        linkUpdateProcessor.process(request);

        verify(bot, times(listIds.size())).sendMessage(any(MessageResponse.class));
    }

    @Test
    public void testProcess_InvalidRequest() {
        LinkUpdateRequest request = new LinkUpdateRequest();
        request.setTgChatIds(List.of());
        request.setDescription("");

        assertThrows(InvalidLinkUpdateException.class, () -> linkUpdateProcessor.process(request));
    }
}
