package edu.java.scrapper.api.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class LinkUpdateRequest {
    private Long id;
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
