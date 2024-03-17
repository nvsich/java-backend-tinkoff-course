package edu.java.scrapper.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkUpdateRequest {
    private Long id;
    private String url;
    private String description;
    private List<Long> tgChatIds;
}
