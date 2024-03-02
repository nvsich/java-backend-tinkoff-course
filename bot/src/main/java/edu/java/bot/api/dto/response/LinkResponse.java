package edu.java.bot.api.dto.response;

import java.net.URI;
import lombok.Data;

@Data
public class LinkResponse {
    private Long id;
    private URI url;
}
