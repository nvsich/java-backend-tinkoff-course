package edu.java.scrapper.entity.factory;

import edu.java.scrapper.entity.Link;
import java.net.ConnectException;
import java.net.URISyntaxException;

public interface LinkFactory {
    Link createLink(String url) throws URISyntaxException, ConnectException;
}
