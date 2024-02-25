package edu.java.bot.entity.factory.impl;

import edu.java.bot.entity.Link;
import edu.java.bot.entity.enums.LinkDomain;
import edu.java.bot.entity.factory.LinkFactory;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LinkFactoryImpl implements LinkFactory {
    private final static String HTTP = "http://";
    private final static String HTTPS = "https://";

    @Override
    @Transactional
    public Link createLink(String url) throws URISyntaxException, ConnectException {
        if (!isValidLink(url)) {
            throw new URISyntaxException(url, "Given url is not correct");
        }

        if (!isReachableUrl(url)) {
            throw new ConnectException("Website is not reachable");
        }

        for (LinkDomain linkDomain : LinkDomain.values()) {
            if (linkDomain.isDomainForUrl(url)) {
                return new Link(linkDomain, url);
            }
        }

        return null;
    }

    private boolean isValidLink(String url) {
        String fullUrl = ((!url.startsWith(HTTP) && !url.startsWith(HTTPS)) ? HTTPS : "") + url;

        UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES);
        return validator.isValid(fullUrl);
    }

    private boolean isReachableUrl(String url) {
        String fullUrl = ((!url.startsWith(HTTP) && !url.startsWith(HTTPS)) ? HTTPS : "") + url;

        try {
            new URI(fullUrl).toURL().openStream().close();
            return true;
        } catch (IOException | URISyntaxException e) {
            return false;
        }
    }
}
