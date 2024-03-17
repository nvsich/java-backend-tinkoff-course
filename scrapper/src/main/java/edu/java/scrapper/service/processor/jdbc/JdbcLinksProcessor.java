package edu.java.scrapper.service.processor.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.enums.LinkDomain;
import edu.java.scrapper.entity.factory.LinkFactory;
import edu.java.scrapper.exception.ChatNotFoundException;
import edu.java.scrapper.exception.LinkDomainNotSupportedException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.LinkSyntaxException;
import edu.java.scrapper.repo.ChatLinkRepo;
import edu.java.scrapper.repo.ChatRepo;
import edu.java.scrapper.repo.LinkRepo;
import edu.java.scrapper.service.processor.LinksProcessor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JdbcLinksProcessor implements LinksProcessor {

    private static final String CHAT_NOT_FOUND = "Chat not found";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private LinkRepo linkRepo;
    private ChatRepo chatRepo;
    private ChatLinkRepo chatLinkRepo;
    private LinkFactory linkFactory;

    @Override
    @Transactional
    public List<Link> getAllLinksForChat(Long chatId) {
        Optional<Chat> chat = chatRepo.findByChatId(chatId);

        if (chat.isEmpty()) {
            throw new ChatNotFoundException(CHAT_NOT_FOUND);
        }

        return chatLinkRepo.findAllLinksForChat(chatId);
    }

    @Override
    @Transactional
    public Link addLinkToChat(Long chatId, String url) {
        Optional<Chat> chat = chatRepo.findByChatId(chatId);

        if (chat.isEmpty()) {
            throw new ChatNotFoundException(CHAT_NOT_FOUND);
        }

        Link link = linkFactory.createLink(url);

        if (link.getLinkDomain().equals(LinkDomain.NOT_SUPPORTED)) {
            throw new LinkDomainNotSupportedException("This domain is not supported");
        }

        linkRepo.save(link);
        chatLinkRepo.save(chatId, linkRepo.findByUrl(link.getUrl()).get().getId());
        return link;
    }

    @Override
    @Transactional
    public Link deleteLinkForChat(Long chatId, String url) {
        Optional<Chat> chat = chatRepo.findByChatId(chatId);

        if (chat.isEmpty()) {
            throw new ChatNotFoundException(CHAT_NOT_FOUND);
        }

        URI uri;
        try {
            String fullUrl = ((!url.startsWith(HTTP) && !url.startsWith(HTTPS)) ? HTTPS : "") + url;
            uri = new URI(fullUrl);
        } catch (URISyntaxException e) {
            throw new LinkSyntaxException("Given url is not correct");
        }

        Optional<Link> link = linkRepo.findByUrl(uri);

        if (link.isEmpty()) {
            throw new LinkNotFoundException("Link not found");
        }

        chatLinkRepo.remove(chatId, link.get().getId());

        return link.get();
    }
}
