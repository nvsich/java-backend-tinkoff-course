package edu.java.bot.repo.mapper;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.enums.ChatStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ChatStateRowMapper implements RowMapper<ChatState> {
    @Override
    public ChatState mapRow(ResultSet rs, int rowNum) throws SQLException {
        var chatId = rs.getLong("chat_id");
        var chatStatus = ChatStatus.valueOf(rs.getString("chat_status"));

        return new ChatState(chatId, chatStatus);
    }
}
