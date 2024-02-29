package edu.java.bot.entity.factory;

import edu.java.bot.entity.Link;
import java.net.ConnectException;
import java.net.URISyntaxException;

public interface LinkFactory {
    Link createLink(String url) throws URISyntaxException, ConnectException;
}
