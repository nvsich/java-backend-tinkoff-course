package edu.java.scrapper.repo.jdbc;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.repo.LinkRepo;
import edu.java.scrapper.repo.mapper.LinkRowMapper;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcLinkRepo implements LinkRepo {

    private static final String SQL_FIND_BY_URL = "SELECT * FROM links WHERE url = ?";
    private static final String SQL_SAVE = "INSERT INTO links (link_domain, url, updated_at) VALUES (?, ?, ?)";
    private static final String SQL_FIND_ALL = "SELECT * FROM links";
    private static final String SQL_DELETE_BY_URL = "DELETE FROM links WHERE url = ?";
    private static final String SQL_FIND_OUTDATED_LINKS = "SELECT * FROM links WHERE updated_at < ?";
    private static final String SQL_UPDATE_UPDATED_AT = "UPDATE links set updated_at = ? WHERE url = ?";

    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Optional<Link> findByUrl(URI url) {
        var links = jdbcTemplate.query(SQL_FIND_BY_URL, new LinkRowMapper(), url.toString());

        return links.isEmpty() ? Optional.empty() : Optional.of(links.getFirst());
    }

    @Override
    @Transactional
    public Link save(Link link) {
        jdbcTemplate.update(SQL_SAVE,
            link.getLinkDomain().toString(),
            link.getUrl().toString(),
            OffsetDateTime.MIN.truncatedTo(ChronoUnit.MILLIS)
        );
        return findByUrl(link.getUrl()).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new LinkRowMapper());
    }

    @Override
    @Transactional
    public Link deleteByUrl(URI url) {
        var link = findByUrl(url);
        link.ifPresent(value -> jdbcTemplate.update(SQL_DELETE_BY_URL, value.getUrl().toString()));
        return link.get();
    }

    @Override
    @Transactional
    public List<Link> findOutdatedLinks(Duration interval) {
        var intervalTime = LocalDateTime.now().minus(interval).atOffset(OffsetDateTime.now().getOffset());
        return jdbcTemplate.query(SQL_FIND_OUTDATED_LINKS, new LinkRowMapper(), intervalTime);
    }

    @Override
    @Transactional
    public void changeUpdatedAt(URI uri, OffsetDateTime updatedAt) {
        jdbcTemplate.update(SQL_UPDATE_UPDATED_AT, updatedAt, uri.toString());
    }
}
