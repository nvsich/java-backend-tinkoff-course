package edu.java.scrapper.repo.jdbc;

import edu.java.scrapper.entity.ChatLink;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.repo.ChatLinkRepo;
import edu.java.scrapper.repo.mapper.ChatLinksRowMapper;
import edu.java.scrapper.repo.mapper.LinkRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcChatLinkRepo implements ChatLinkRepo {

    private static final String SQL_ADD = "INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)";
    private static final String SQL_REMOVE = "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM chat_link";
    private static final String SQL_FIND_BY_IDS = "SELECT * FROM chat_link WHERE chat_id = ? AND link_id = ?";
    private static final String SQL_FIND_ALL_CHATS_FOR_LINK =
        "SELECT chat_link.chat_id FROM chat_link WHERE link_id = ?";
    private static final String SQL_FIND_ALL_LINKS_FOR_CHAT =
        "SELECT * from links l JOIN chat_link cl on l.link_id = cl.link_id WHERE cl.chat_id = ?";

    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void save(long chatId, long linkId) {
        jdbcTemplate.update(SQL_ADD, chatId, linkId);
    }

    @Override
    @Transactional
    public void remove(long chatId, long linkId) {
        jdbcTemplate.update(SQL_REMOVE, chatId, linkId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatLink> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new ChatLinksRowMapper());
    }

    @Override
    @Transactional
    public Optional<ChatLink> findByIds(long chatId, long linkId) {
        var result = jdbcTemplate.query(SQL_FIND_BY_IDS, new ChatLinksRowMapper(), chatId, linkId);

        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    @Override
    @Transactional
    public List<Long> findAllChatsForLink(long linkId) {
        return jdbcTemplate.queryForList(SQL_FIND_ALL_CHATS_FOR_LINK, Long.class, linkId);
    }

    @Override
    @Transactional
    public List<Link> findAllLinksForChat(long chatId) {
        return jdbcTemplate.query(SQL_FIND_ALL_LINKS_FOR_CHAT, new LinkRowMapper(), chatId);
    }
}
