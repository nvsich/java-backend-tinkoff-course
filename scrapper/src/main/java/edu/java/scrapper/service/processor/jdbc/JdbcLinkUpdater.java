package edu.java.scrapper.service.processor.jdbc;

import edu.java.scrapper.api.client.BotClient;
import edu.java.scrapper.dto.request.LinkUpdateRequest;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.enums.LinkDomain;
import edu.java.scrapper.repo.ChatLinkRepo;
import edu.java.scrapper.repo.LinkRepo;
import edu.java.scrapper.service.client.GitHubWebClient;
import edu.java.scrapper.service.client.StackOverflowWebClient;
import edu.java.scrapper.service.processor.LinkUpdater;
import java.time.Duration;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {

    private static final Duration OUTDATED_LINKS_INTERVAL = Duration.ofMinutes(10);
    private LinkRepo linkRepo;
    private ChatLinkRepo chatLinkRepo;
    private GitHubWebClient gitHubWebClient;
    private StackOverflowWebClient stackOverflowWebClient;
    private BotClient botClient;

    @Override
    @Transactional
    public int update() {
        var updatesCount = 0;

        var outdatedLinks = linkRepo.findOutdatedLinks(OUTDATED_LINKS_INTERVAL);

        for (var link : outdatedLinks) {
            var lastUpdatedAt = switch (link.getLinkDomain()) {
                case GITHUB -> getLastUpdatedAtGithub(link);
                case STACKOVERFLOW -> getLastUpdatedAtStackOverflow(link);
                case NOT_SUPPORTED -> link.getUpdatedAt();
            };

            if (lastUpdatedAt.isAfter(link.getUpdatedAt())) {
                botClient.sendUpdate(LinkUpdateRequest.builder()
                    .id(link.getId())
                    .url(link.getUrl().toString())
                    .description("Link has been updated")
                    .tgChatIds(chatLinkRepo.findAllChatsForLink(link.getId()))
                    .build()
                );

                updatesCount++;
            }
        }

        return updatesCount;
    }

    private OffsetDateTime getLastUpdatedAtGithub(Link link) {
        var credentials = LinkDomain.GITHUB.getCredentials(link.getUrl());
        var githubRepoResponse = gitHubWebClient.fetchRepo(credentials.get("ownerName"), credentials.get("repoName"));

        return githubRepoResponse.updatedAt();
    }

    private OffsetDateTime getLastUpdatedAtStackOverflow(Link link) {
        var credentials = LinkDomain.STACKOVERFLOW.getCredentials(link.getUrl());
        var stackOverflowResponse = stackOverflowWebClient.fetchQuestion(credentials.get("questionId"));

        return stackOverflowResponse.itemList().getFirst().lastActivityDate();
    }
}
