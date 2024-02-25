package edu.java.bot.repo.stub_impl;

import edu.java.bot.repo.ChatLinksRepo;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ChatLinksRepoStub implements ChatLinksRepo {

    private static final HashMap<Long, Set<Long>> DATABASE = new HashMap<>();

    @Override
    @Transactional
    public Set<Long> findById(Long id) {
        if (DATABASE.containsKey(id)) {
            return DATABASE.get(id);
        }

        return Set.of();
    }

    @Override
    @Transactional
    public void save(Long chatId, Long linkId) {
        if (!DATABASE.containsKey(chatId)) {
            DATABASE.put(chatId, new HashSet<>());
        }

        DATABASE.get(chatId).add(linkId);
    }

    @Override
    public void deleteLinkId(Long chatId, Long linkId) {
        DATABASE.get(chatId).remove(linkId);
    }
}
