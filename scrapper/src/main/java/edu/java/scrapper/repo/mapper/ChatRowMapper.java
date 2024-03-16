package edu.java.scrapper.repo.mapper;

import edu.java.scrapper.entity.Chat;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatRowMapper implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        long chatId = rs.getLong("chat_id");
        return new Chat(id, chatId);
    }
}
