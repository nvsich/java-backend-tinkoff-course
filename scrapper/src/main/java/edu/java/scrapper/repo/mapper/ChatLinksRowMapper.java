package edu.java.scrapper.repo.mapper;

import edu.java.scrapper.entity.ChatLink;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ChatLinksRowMapper implements RowMapper<ChatLink> {
    @Override
    public ChatLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        var chatId = rs.getLong("chat_id");
        var linkId = rs.getLong("link_id");

        return new ChatLink(chatId, linkId);
    }
}
