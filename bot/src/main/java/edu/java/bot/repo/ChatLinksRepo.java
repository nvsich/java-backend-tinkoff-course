package edu.java.bot.repo;

import java.util.Set;

public interface ChatLinksRepo {

    Set<Long> findById(Long id);

    void save(Long chatId, Long linkId);

    void deleteLinkId(Long chatId, Long linkId);

}
