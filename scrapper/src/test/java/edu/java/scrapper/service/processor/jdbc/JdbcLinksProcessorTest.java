package edu.java.scrapper.service.processor.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.enums.LinkDomain;
import edu.java.scrapper.entity.factory.LinkFactory;
import edu.java.scrapper.exception.ChatNotFoundException;
import edu.java.scrapper.exception.LinkDomainNotSupportedException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.LinkSyntaxException;
import edu.java.scrapper.repo.jdbc.JdbcChatLinkRepo;
import edu.java.scrapper.repo.jdbc.JdbcChatRepo;
import edu.java.scrapper.repo.jdbc.JdbcLinkRepo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcLinksProcessorTest {

    @Mock
    private JdbcChatRepo chatRepo;

    @Mock
    private JdbcLinkRepo linkRepo;

    @Mock
    private JdbcChatLinkRepo chatLinkRepo;

    @Mock
    private LinkFactory linkFactory;

    @InjectMocks
    private JdbcLinksProcessor jdbcLinksProcessor;


    @Test
    void testGetAllLinksForChat_ChatFound() {
        var chatId = 1L;
        List<Link> links = new ArrayList<>();
        links.add(new Link());

        when(chatRepo.findByChatId(chatId)).thenReturn(Optional.of(new Chat()));
        when(chatLinkRepo.findAllLinksForChat(chatId)).thenReturn(links);

        List<Link> result = jdbcLinksProcessor.getAllLinksForChat(chatId);

        assertEquals(links, result);
        verify(chatRepo).findByChatId(chatId);
        verify(chatLinkRepo).findAllLinksForChat(chatId);
    }

    @Test
    void testGetAllLinksForChat_ChatNotFound() {
        var chatId = 1L;

        when(chatRepo.findByChatId(chatId)).thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> jdbcLinksProcessor.getAllLinksForChat(chatId));
        verify(chatRepo).findByChatId(chatId);
    }

    @Test
    void testAddLinkToChat_Success() {
        var chatId = 1L;
        String url = "https://example.com";
        Link link = new Link();
        link.setId(123L);
        link.setUrl(URI.create(url));
        link.setLinkDomain(LinkDomain.GITHUB);

        when(chatRepo.findByChatId(chatId)).thenReturn(Optional.of(new Chat()));
        when(linkFactory.createLink(url)).thenReturn(link);
        when(linkRepo.save(link)).thenReturn(link);
        when(linkRepo.findByUrl(URI.create(url))).thenReturn(Optional.of(link));

        Link result = jdbcLinksProcessor.addLinkToChat(chatId, url);

        assertEquals(link, result);
        verify(chatRepo).findByChatId(chatId);
        verify(linkFactory).createLink(url);
        verify(linkRepo).save(link);
        verify(chatLinkRepo).save(chatId, link.getId());
    }

    @Test
    void testAddLinkToChat_ChatNotFound() {
        var chatId = 1L;
        String url = "https://example.com";

        when(chatRepo.findByChatId(chatId)).thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> jdbcLinksProcessor.addLinkToChat(chatId, url));
        verify(chatRepo).findByChatId(chatId);
    }

    @Test
    void testAddLinkToChat_LinkDomainNotSupported() {
        var chatId = 1L;
        String url = "https://example.com";
        Link link = new Link();
        link.setUrl(URI.create(url));
        link.setLinkDomain(LinkDomain.NOT_SUPPORTED);

        when(chatRepo.findByChatId(chatId)).thenReturn(Optional.of(new Chat()));
        when(linkFactory.createLink(url)).thenReturn(link);

        assertThrows(LinkDomainNotSupportedException.class, () -> jdbcLinksProcessor.addLinkToChat(chatId, url));
        verify(chatRepo).findByChatId(chatId);
        verify(linkFactory).createLink(url);
    }

    @Test
    void testDeleteLinkForChat_Success() throws URISyntaxException {
        var chatId = 1L;
        String url = "https://example.com";
        Link link = new Link();
        link.setId(123L);
        link.setUrl(URI.create(url));

        when(chatRepo.findByChatId(chatId)).thenReturn(Optional.of(new Chat()));
        when(linkRepo.findByUrl(new URI(url))).thenReturn(Optional.of(link));

        Link result = jdbcLinksProcessor.deleteLinkForChat(chatId, url);

        assertEquals(link, result);
        verify(chatRepo).findByChatId(chatId);
        verify(linkRepo).findByUrl(new URI(url));
        verify(chatLinkRepo).remove(chatId, link.getId());
    }

    @Test
    void testDeleteLinkForChat_ChatNotFound() throws URISyntaxException {
        var chatId = 1L;
        String url = "https://example.com";

        when(chatRepo.findByChatId(chatId)).thenReturn(Optional.empty());

        assertThrows(ChatNotFoundException.class, () -> jdbcLinksProcessor.deleteLinkForChat(chatId, url));
        verify(chatRepo).findByChatId(chatId);
    }

    @Test
    void testDeleteLinkForChat_LinkNotFound() throws URISyntaxException {
        var chatId = 1L;
        String url = "https://example.com";

        when(chatRepo.findByChatId(chatId)).thenReturn(Optional.of(new Chat()));
        when(linkRepo.findByUrl(new URI(url))).thenReturn(Optional.empty());

        assertThrows(LinkNotFoundException.class, () -> jdbcLinksProcessor.deleteLinkForChat(chatId, url));
        verify(chatRepo).findByChatId(chatId);
        verify(linkRepo).findByUrl(new URI(url));
    }

    @Test
    void testDeleteLinkForChat_LinkSyntaxException() {
        var chatId = 1L;
        String url = "invalid url";

        when(chatRepo.findByChatId(chatId)).thenReturn(Optional.of(new Chat()));

        assertThrows(LinkSyntaxException.class, () -> jdbcLinksProcessor.deleteLinkForChat(chatId, url));
    }
}
