package edu.java.scrapper.entity.factory.impl;

import edu.java.scrapper.entity.enums.LinkDomain;
import edu.java.scrapper.exception.LinkIsNotReachableException;
import edu.java.scrapper.exception.LinkSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkFactoryImplTest {
    private LinkFactoryImpl linkFactory;

    @BeforeEach
    void setUp() {
        linkFactory = new LinkFactoryImpl();
    }

    @Test
    void createLink_ThrowsIncorrectSyntax() {
        String url = "test incorrect syntax";

        assertThrows(LinkSyntaxException.class, () -> linkFactory.createLink(url));
    }

    @Test
    void createLink_ThrowsNotReachable() {
        String url = "testNotReachable";

        assertThrows(LinkIsNotReachableException.class, () -> linkFactory.createLink(url));
    }

    @Test
    void createLink_CreatesDomain() {
        String url = "example.com";

        var result = linkFactory.createLink(url);

        assertEquals(LinkDomain.NOT_SUPPORTED, result.getLinkDomain());
    }
}
