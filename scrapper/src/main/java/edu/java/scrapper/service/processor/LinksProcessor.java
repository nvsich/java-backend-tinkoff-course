package edu.java.scrapper.service.processor;

import edu.java.scrapper.entity.Link;
import java.util.List;

public interface LinksProcessor {
    List<Link> getAllLinksForChat(Long chatId);

    Link addLinkToChat(Long chatId, String url);

    Link deleteLinkForChat(Long chatId, String url);
}
