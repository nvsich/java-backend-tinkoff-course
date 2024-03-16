package edu.java.scrapper.repo.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.ScrapperApplication;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.enums.LinkDomain;
import java.net.URI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ScrapperApplication.class)
@ActiveProfiles("test")
class JdbcChatLinkRepoTest extends IntegrationTest {

    @Autowired
    private JdbcChatLinkRepo jdbcChatLinkRepo;

    @Autowired
    private JdbcChatRepo jdbcChatRepo;

    @Autowired
    private JdbcLinkRepo jdbcLinkRepo;

    private static final Long CHAT_ID = 123L;
    private Long linkId;
    private static final String URL_EXAMPLE = "https://example.com";

    @BeforeEach
    void setUp() {
        var chat = new Chat();
        chat.setChatId(CHAT_ID);
        jdbcChatRepo.save(chat);

        var link = new Link();
        link.setUrl(URI.create(URL_EXAMPLE));
        link.setLinkDomain(LinkDomain.NOT_SUPPORTED);

        var savedLink = jdbcLinkRepo.save(link);
        linkId = savedLink.getId();
    }

    @Test
    @Transactional
    @Rollback
    void findAll_ShouldReturnEmpty() {
        var result = jdbcChatLinkRepo.findAll();
        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    @Transactional
    @Rollback
    void save_ShouldSaveEntity() {
        jdbcChatLinkRepo.save(CHAT_ID, linkId);

        Assertions.assertEquals(CHAT_ID, jdbcChatLinkRepo.findAll().getFirst().getChatId());
        Assertions.assertEquals(linkId, jdbcChatLinkRepo.findAll().getFirst().getLinkId());
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        jdbcChatLinkRepo.save(CHAT_ID, linkId);
        jdbcChatLinkRepo.remove(CHAT_ID, linkId);

        Assertions.assertTrue(jdbcChatLinkRepo.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findByIds() {
        jdbcChatLinkRepo.save(CHAT_ID, linkId);
        var result = jdbcChatLinkRepo.findByIds(CHAT_ID, linkId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatsForLink() {
        jdbcChatLinkRepo.save(CHAT_ID, linkId);
        var result = jdbcChatLinkRepo.findAllChatsForLink(linkId);

        Assertions.assertEquals(CHAT_ID, result.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    void findAllLinksForChat() {
        jdbcChatLinkRepo.save(CHAT_ID, linkId);
        var result = jdbcChatLinkRepo.findAllLinksForChat(CHAT_ID);

        Assertions.assertEquals(URL_EXAMPLE, result.getFirst().getUrl().toString());
    }
}
