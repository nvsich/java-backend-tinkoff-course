package edu.java.scrapper.repo;

import edu.java.scrapper.entity.ChatLink;
import edu.java.scrapper.entity.Link;
import java.util.List;
import java.util.Optional;

public interface ChatLinkRepo {
    void save(long chatId, long linkId);

    void remove(long chatId, long linkId);

    List<ChatLink> findAll();

    Optional<ChatLink> findByIds(long chatId, long linkId);

    List<Long> findAllChatsForLink(long linkId);

    List<Link> findAllLinksForChat(long chatId);
}
