package edu.java.scrapper.entity.factory.impl;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.enums.LinkDomain;
import edu.java.scrapper.entity.factory.LinkFactory;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LinkFactoryImpl implements LinkFactory {
    private final static String HTTP = "http://";
    private final static String HTTPS = "https://";

    @Override
    @Transactional
    public Link createLink(String url) throws URISyntaxException, ConnectException {
        String fullUrl = ((!url.startsWith(HTTP) && !url.startsWith(HTTPS)) ? HTTPS : "") + url;

        if (!isValidLink(fullUrl)) {
            throw new URISyntaxException(url, "Given url is not correct");
        }

        if (!isReachableUrl(fullUrl)) {
            throw new ConnectException("Website is not reachable");
        }

        URI uri = new URI(fullUrl);

        for (LinkDomain linkDomain : LinkDomain.values()) {
            if (linkDomain.isDomainForUrl(fullUrl)) {
                Link link = new Link();
                link.setLinkDomain(linkDomain);
                link.setUrl(uri);
                return link;
            }
        }

        return null;
    }

    private boolean isValidLink(String url) {
        try {
            URI uri = new URI(url);
            URL validatedURL = uri.toURL();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    private boolean isReachableUrl(String url) {
        try {
            new URI(url).toURL().openStream().close();
            return true;
        } catch (IOException | URISyntaxException e) {
            return false;
        }
    }
}