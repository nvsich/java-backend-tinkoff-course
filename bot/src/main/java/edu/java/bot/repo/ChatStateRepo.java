package edu.java.bot.repo;

import edu.java.bot.entity.ChatState;
import java.util.List;
import java.util.Optional;

public interface ChatStateRepo {

    Optional<ChatState> findByChatId(Long chatId);

    void save(ChatState chatState);

    void deleteByChatId(Long id);

    List<ChatState> findAll();
}
