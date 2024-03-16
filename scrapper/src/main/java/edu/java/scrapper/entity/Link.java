package edu.java.scrapper.entity;

import edu.java.scrapper.entity.enums.LinkDomain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "link_id")
    private Long id;

    @Enumerated
    @Column(name = "link_domain")
    private LinkDomain linkDomain;

    @Column(name = "url")
    private URI url;

}
