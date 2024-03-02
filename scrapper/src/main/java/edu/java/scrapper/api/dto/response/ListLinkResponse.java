package edu.java.scrapper.api.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class ListLinkResponse {
    private List<LinkResponse> links;
    private int size;
}
