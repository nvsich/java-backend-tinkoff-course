package edu.java.scrapper.repo.mapper;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.enums.LinkDomain;
import org.springframework.jdbc.core.RowMapper;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        var id = rs.getLong("link_id");
        var linkDomain = LinkDomain.valueOf(rs.getString("link_domain"));
        var uri = URI.create(rs.getString("url"));

        return new Link(id, linkDomain, uri);
    }
}
