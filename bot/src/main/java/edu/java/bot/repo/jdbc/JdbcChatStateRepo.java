package edu.java.bot.repo.jdbc;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.enums.ChatStatus;
import edu.java.bot.repo.ChatStateRepo;
import java.util.List;
import java.util.Optional;
import edu.java.bot.repo.mapper.ChatStateRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcChatStateRepo implements ChatStateRepo {

    private JdbcTemplate jdbcTemplate;

    private static final String SQL_FIND_BY_CHAT_ID = "SELECT * FROM chat_states WHERE chat_id = ?";

    private static final String SQL_SAVE = "INSERT INTO chat_states (chat_id, chat_status) VALUES (?, ?)";

    private static final String SQL_DELETE = "DELETE FROM chat_states WHERE chat_id = ?";

    private static final String SQL_FIND_ALL = "SELECT * FROM chat_states";

    @Override
    @Transactional(readOnly = true)
    public Optional<ChatState> findByChatId(Long chatId) {
        var chatStates = jdbcTemplate.query(SQL_FIND_BY_CHAT_ID, new ChatStateRowMapper(), chatId);

        return chatStates.isEmpty() ? Optional.empty() : Optional.of(chatStates.getFirst());
    }

    @Override
    @Transactional
    public void save(ChatState chatState) {
        jdbcTemplate.update(SQL_SAVE,
            chatState.getChatId(),
            chatState.getChatStatus().name()
        );
    }

    @Override
    @Transactional
    public void deleteByChatId(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatState> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new ChatStateRowMapper());
    }
}
