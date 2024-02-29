package edu.java.bot.entity;

import edu.java.bot.entity.enums.LinkDomain;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Link {
    //Stub while database is not implemented.
    private static Long idLinkCounter = 1L;
    private Long id;
    private LinkDomain linkDomain;
    private String url;

    public Link(LinkDomain linkDomain, String url) {
        this.linkDomain = linkDomain;
        this.url = url;
        this.id = idLinkCounter++;
    }

}
