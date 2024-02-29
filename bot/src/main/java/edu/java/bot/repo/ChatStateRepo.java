package edu.java.bot.repo;

import edu.java.bot.entity.ChatState;
import java.util.Optional;

public interface ChatStateRepo {

    Optional<ChatState> findById(Long id);

    void save(Long id, ChatState chatState);

}
