package edu.java.scrapper.repo.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.repo.ChatRepo;
import edu.java.scrapper.repo.mapper.ChatRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcChatRepo implements ChatRepo {

    private static final String SQL_FIND_BY_CHAT_ID = "SELECT * FROM chats WHERE chat_id = ?";
    private static final String SQL_DELETE_BY_CHAT_ID = "DELETE FROM chats WHERE chat_id = ?";
    private static final String SQL_SAVE = "INSERT INTO chats (chat_id) VALUES (?)";
    private static final String SQL_FIND_ALL = "SELECT * FROM chats";

    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Optional<Chat> findByChatId(long chatId) {
        var result = jdbcTemplate.query(SQL_FIND_BY_CHAT_ID, new ChatRowMapper(), chatId);

        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    @Override
    @Transactional
    public void deleteByChatId(long chatId) {
        jdbcTemplate.update(SQL_DELETE_BY_CHAT_ID, chatId);
    }

    @Override
    @Transactional
    public void save(Chat chat) {
        jdbcTemplate.update(SQL_SAVE, chat.getChatId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chat> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new ChatRowMapper());
    }
}
