package edu.java.scrapper.repo;

import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepo {

    Optional<Link> findByUrl(URI url);

    Link save(Link link);

    List<Link> findAll();

    Link deleteByUrl(URI url);

    List<Link> findOutdatedLinks(Duration interval);

    void changeUpdatedAt(URI uri, OffsetDateTime updatedAt);

}
