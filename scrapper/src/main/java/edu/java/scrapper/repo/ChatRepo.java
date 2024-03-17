package edu.java.scrapper.repo;

import edu.java.scrapper.entity.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepo {

    Optional<Chat> findByChatId(long chatId);

    void deleteByChatId(long chatId);

    void save(Chat chat);

    List<Chat> findAll();
}
