package edu.java.scrapper.repo;

import edu.java.scrapper.entity.Chat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat, Long> {

    Optional<Chat> findByChatId(long chatId);

    void deleteByChatId(long chatId);
}
