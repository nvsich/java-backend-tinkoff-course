package edu.java.bot.repo.mapper;

import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.enums.ChatStatus;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatStateRowMapper implements RowMapper<ChatState> {
    @Override
    public ChatState mapRow(ResultSet rs, int rowNum) throws SQLException {
        var id = rs.getLong("chat_state_id");
        var chatId = rs.getLong("chat_id");
        var chatStatus = ChatStatus.valueOf(rs.getString("chat_status"));

        return new ChatState(id, chatId, chatStatus);
    }
}
