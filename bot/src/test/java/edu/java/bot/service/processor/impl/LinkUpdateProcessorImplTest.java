package edu.java.bot.service.processor.impl;

import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.exception.InvalidLinkUpdateException;
import edu.java.bot.telegrambot.Bot;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LinkUpdateProcessorImplTest {

    @Mock
    private Bot bot;

    @InjectMocks
    private LinkUpdateProcessorImpl linkUpdateProcessor;

    @Test
    void process_ValidRequest_SendsMessageToEachChatId() {
        LinkUpdateRequest request = new LinkUpdateRequest();
        request.setTgChatIds(List.of(1L, 2L));
        request.setDescription("description");

        linkUpdateProcessor.process(request);

        verify(bot, times(2)).sendMessage(ArgumentMatchers.any());
    }

    @Test
    void process_NullRequest_ThrowsInvalidLinkUpdateException() {
        assertThrows(InvalidLinkUpdateException.class, () -> linkUpdateProcessor.process(null));
    }

    @Test
    void process_EmptyChatIds_ThrowsInvalidLinkUpdateException() {
        LinkUpdateRequest request = new LinkUpdateRequest();

        assertThrows(InvalidLinkUpdateException.class, () -> linkUpdateProcessor.process(request));
    }

    @Test
    void process_BlankDescription_ThrowsInvalidLinkUpdateException() {
        LinkUpdateRequest request = new LinkUpdateRequest();
        request.setTgChatIds(List.of(1L));
        request.setDescription(" ");

        assertThrows(InvalidLinkUpdateException.class, () -> linkUpdateProcessor.process(request));
    }
}
