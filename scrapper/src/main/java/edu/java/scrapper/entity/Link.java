package edu.java.scrapper.entity;

import edu.java.scrapper.entity.enums.LinkDomain;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.net.URI;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long id;

    @Enumerated
    @Column(name = "link_domain")
    private LinkDomain linkDomain;

    @Column(name = "url")
    private URI url;

    @ElementCollection
    private Set<Long> chatIds;

}
