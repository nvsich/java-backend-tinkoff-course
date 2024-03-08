package edu.java.scrapper.service.processor.impl;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.enums.LinkDomain;
import edu.java.scrapper.entity.factory.LinkFactory;
import edu.java.scrapper.exception.ChatNotFoundException;
import edu.java.scrapper.exception.LinkDomainNotSupportedException;
import edu.java.scrapper.exception.LinkIsNotReachableException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.LinkSyntaxException;
import edu.java.scrapper.repo.ChatRepo;
import edu.java.scrapper.repo.LinkRepo;
import edu.java.scrapper.service.processor.LinksProcessor;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinksProcessorImpl implements LinksProcessor {

    private LinkRepo linkRepo;

    private ChatRepo chatRepo;

    private LinkFactory linkFactory;

    private static final String CHAT_NOT_FOUND = "Chat not found";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    @Override
    public List<Link> getAllLinksForChat(Long chatId) {
        Optional<Chat> chat = chatRepo.findByChatId(chatId);

        if (chat.isEmpty()) {
            throw new ChatNotFoundException(CHAT_NOT_FOUND);
        }

        return linkRepo.findAllById(chat.get().getLinkIds());
    }

    @Override
    public Link addLinkToChat(Long chatId, String url) {
        Optional<Chat> chat = chatRepo.findByChatId(chatId);

        if (chat.isEmpty()) {
            throw new ChatNotFoundException(CHAT_NOT_FOUND);
        }

        Link link;
        try {
            link = linkFactory.createLink(url);
        } catch (URISyntaxException e) {
            throw new LinkSyntaxException(e.getMessage());
        } catch (ConnectException e) {
            throw new LinkIsNotReachableException(e.getMessage());
        }

        if (link.getLinkDomain().equals(LinkDomain.NOT_SUPPORTED)) {
            throw new LinkDomainNotSupportedException("This domain is not supported");
        }

        chat.get().getLinkIds().add(link.getId());
        link.getChatIds().add(chatId);

        return link;
    }

    @Override
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

        chat.get().getLinkIds().remove(link.get().getId());

        return link.get();
    }
}
