package edu.java.bot.repo.jdbc;

import edu.java.bot.BotApplication;
import edu.java.bot.IntegrationTest;
import edu.java.bot.entity.ChatState;
import edu.java.bot.entity.enums.ChatStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = BotApplication.class)
@ActiveProfiles("test")
public class JdbcChatStateRepoTest extends IntegrationTest {
    @Autowired
    private JdbcChatStateRepo jdbcChatStateRepo;

    @Test
    @Transactional
    @Rollback
    void findAll_ShouldReturnEmpty() {
        var result = jdbcChatStateRepo.findAll();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findByChatId_ShouldReturnOptionalEmpty() {
        var result = jdbcChatStateRepo.findByChatId(1L);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void saveAndFindByChatId_ShouldReturnOptionalOfChatState() {
        ChatState chatState = ChatState.builder()
                .chatId(1L)
                .chatStatus(ChatStatus.WAITING_FOR_COMMAND)
                .build();

        jdbcChatStateRepo.save(chatState);

        var result = jdbcChatStateRepo.findByChatId(1L);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void delete_ShouldDelete() {
        ChatState chatState = ChatState.builder()
                .chatId(1L)
                .chatStatus(ChatStatus.WAITING_FOR_LINK_TO_UNTRACK)
                .build();

        jdbcChatStateRepo.save(chatState);

        jdbcChatStateRepo.deleteByChatId(1L);

        var result = jdbcChatStateRepo.findAll();
        Assertions.assertTrue(result.isEmpty());
    }
}
