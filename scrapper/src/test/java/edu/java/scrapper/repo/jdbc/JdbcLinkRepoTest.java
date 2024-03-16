package edu.java.scrapper.repo.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.ScrapperApplication;
import edu.java.scrapper.entity.factory.LinkFactory;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = ScrapperApplication.class)
@ActiveProfiles("test")
class JdbcLinkRepoTest extends IntegrationTest {

    @Autowired
    private JdbcLinkRepo jdbcLinkRepo;

    @Autowired
    private LinkFactory linkFactory;

    private static final String EXAMPLE_URL = "https://example.com";

    @Test
    @Transactional
    @Rollback
    void findAll_ShouldReturnEmpty() {
        var result = jdbcLinkRepo.findAll();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void save_ShouldSaveLink() {
        var link = linkFactory.createLink(EXAMPLE_URL);

        var result = jdbcLinkRepo.save(link);

        Assertions.assertEquals(result.getUrl().toString(), EXAMPLE_URL);
    }

    @Test
    @Transactional
    @Rollback
    void findByUrl_ShouldFindLinkByUrl() {
        var link = linkFactory.createLink(EXAMPLE_URL);
        var uri = URI.create(EXAMPLE_URL);

        jdbcLinkRepo.save(link);

        var result = jdbcLinkRepo.findByUrl(uri);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void deleteByUrl_ShouldDeleteLinkByUrl() {
        var link = linkFactory.createLink(EXAMPLE_URL);
        var uri = URI.create(EXAMPLE_URL);

        jdbcLinkRepo.save(link);

        var result = jdbcLinkRepo.deleteByUrl(uri);

        Assertions.assertTrue(jdbcLinkRepo.findByUrl(result.getUrl()).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void changeUpdatedAt_ShouldChangeUpdatedAtTime() {
        var link = linkFactory.createLink(EXAMPLE_URL);
        var uri = URI.create(EXAMPLE_URL);

        jdbcLinkRepo.save(link);

        var newTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS).minusMinutes(10);
        jdbcLinkRepo.changeUpdatedAt(uri, newTime);

        Assertions.assertEquals(newTime, jdbcLinkRepo.findByUrl(uri).get().getUpdatedAt());
    }

    @Test
    @Transactional
    @Rollback
    void findOutdatedLinks_ShouldFindOutdatedLinks() {
        var link = linkFactory.createLink(EXAMPLE_URL);
        var uri = URI.create(EXAMPLE_URL);

        jdbcLinkRepo.save(link);

        jdbcLinkRepo.changeUpdatedAt(uri, OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS).minusMinutes(10));

        var result = jdbcLinkRepo.findOutdatedLinks(Duration.ofMinutes(10));

        Assertions.assertEquals(uri.toString(), result.getFirst().getUrl().toString());
    }

    @Test
    @Transactional
    @Rollback
    void findOutdatedLinks_ShouldNotFindNotOutdatedLinks() {
        var link = linkFactory.createLink(EXAMPLE_URL);
        var uri = URI.create(EXAMPLE_URL);

        jdbcLinkRepo.save(link);

        jdbcLinkRepo.changeUpdatedAt(uri, OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS).plusMinutes(10));

        var result = jdbcLinkRepo.findOutdatedLinks(Duration.ofMinutes(10));

        Assertions.assertTrue(result.isEmpty());
    }
}
