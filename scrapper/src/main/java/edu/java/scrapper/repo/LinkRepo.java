package edu.java.scrapper.repo;

import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepo extends JpaRepository<Link, Long> {

    Optional<Link> findByUrl(URI url);

}
