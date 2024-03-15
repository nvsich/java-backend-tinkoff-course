package edu.java.bot.repo;

import edu.java.bot.entity.ChatState;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatStateRepo extends JpaRepository<ChatState, Long> {

    Optional<ChatState> findByChatId(Long chatId);
}
