package edu.java.scrapper.dto.response;

import java.net.URI;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkResponse {
    private Long id;
    private URI url;
}
