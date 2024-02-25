package edu.java.bot.repo.stub_impl;

import edu.java.bot.entity.Link;
import edu.java.bot.repo.LinkRepo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LinkRepoStub implements LinkRepo {

    private static final HashMap<Long, Link> DATABASE = new HashMap<>();

    @Override
    @Transactional
    public List<Link> findAllById(Iterable<Long> ids) {
        List<Link> links = new ArrayList<>();
        for (Long id : ids) {
            if (DATABASE.get(id) != null) {
                links.add(DATABASE.get(id));
            }
        }
        return links;
    }

    @Override
    public void save(Link link) {
        DATABASE.put(link.getId(), link);
    }

    @Override
    public void deleteById(Long linkId) {
        DATABASE.remove(linkId);
    }
}
