package edu.java.bot.repo;

import edu.java.bot.entity.Link;
import java.util.List;

public interface LinkRepo {

    List<Link> findAllById(Iterable<Long> id);

    void save(Link link);

    void deleteById(Long linkId);

}
