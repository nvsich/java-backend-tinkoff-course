package edu.java.scrapper.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListLinkResponse {
    private List<LinkResponse> links;
    private int size;
}
