package edu.java.scrapper.service.processor.jdbc;

import edu.java.scrapper.api.client.BotClient;
import edu.java.scrapper.dto.response.GitHubRepoResponse;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.enums.LinkDomain;
import edu.java.scrapper.repo.jdbc.JdbcChatLinkRepo;
import edu.java.scrapper.repo.jdbc.JdbcLinkRepo;
import edu.java.scrapper.service.client.GitHubWebClient;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcLinkUpdaterTest {

    @Mock
    private JdbcLinkRepo jdbcLinkRepo;
    @Mock
    private JdbcChatLinkRepo jdbcChatLinkRepo;
    @Mock
    private GitHubWebClient gitHubWebClient;
    @Mock
    private BotClient botClient;
    @InjectMocks
    private JdbcLinkUpdater linkUpdater;

    private static final Long ID = 1L;
    private static final String GITHUB_URL = "https://github.com/owner/repo/";
    private static final OffsetDateTime OLD_DATETIME = OffsetDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MILLIS);

    @Test
    void update_ShouldReturnNumberOfUpdates() {
        var githubRepoResponse = mock(GitHubRepoResponse.class);
        var outdatedLinks = List.of(new Link(ID, LinkDomain.GITHUB, URI.create(GITHUB_URL), OLD_DATETIME));

        when(githubRepoResponse.updatedAt()).thenReturn(OffsetDateTime.now());
        when(jdbcLinkRepo.findOutdatedLinks(any())).thenReturn(outdatedLinks);
        when(gitHubWebClient.fetchRepo(any(), any())).thenReturn(githubRepoResponse);

        var result = linkUpdater.update();
        assertEquals(1, result);
    }
}
