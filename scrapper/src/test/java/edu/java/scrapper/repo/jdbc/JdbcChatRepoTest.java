package edu.java.scrapper.repo.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.ScrapperApplication;
import edu.java.scrapper.entity.Chat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ScrapperApplication.class)
@ActiveProfiles("test")
class JdbcChatRepoTest extends IntegrationTest {
    @Autowired
    private JdbcChatRepo jdbcChatRepo;

    @Test
    @Transactional
    @Rollback
    void findAll_ShouldReturnEmpty() {
        var result = jdbcChatRepo.findAll();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findByChatId_ShouldReturnOptionalEmpty() {
        var result = jdbcChatRepo.findByChatId(1L);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void saveAndFindByChatId_ShouldReturnOptionalOfChatState() {
        var chatId = 456L;
        Chat chat = new Chat(123L, chatId);

        jdbcChatRepo.save(chat);

        var result = jdbcChatRepo.findByChatId(chatId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void delete_ShouldDelete() {
        var chatId = 456L;
        Chat chat = new Chat(123L, chatId);

        jdbcChatRepo.save(chat);

        jdbcChatRepo.deleteByChatId(chatId);

        var result = jdbcChatRepo.findAll();
        Assertions.assertTrue(result.isEmpty());
    }

}
