package edu.java.bot.repo.stub_impl;

import edu.java.bot.entity.ChatState;
import edu.java.bot.repo.ChatStateRepo;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ChatStateRepoStub implements ChatStateRepo {

    private static final HashMap<Long, ChatState> DATABASE = new HashMap<>();

    @Override
    @Transactional
    public Optional<ChatState> findById(Long id) {
        if (DATABASE.containsKey(id)) {
            return Optional.ofNullable(DATABASE.get(id));
        }

        return Optional.empty();
    }

    @Override
    public void save(Long id, ChatState chatState) {
        DATABASE.put(id, chatState);
    }
}
