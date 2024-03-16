package edu.java.scrapper.entity.factory;

import edu.java.scrapper.entity.Link;

public interface LinkFactory {
    Link createLink(String url);
}
