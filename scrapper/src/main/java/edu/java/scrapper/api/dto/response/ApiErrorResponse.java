package edu.java.scrapper.api.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class ApiErrorResponse {
    private String description;
    private String code;
    private String exceptionName;
    private String exceptionMessage;
    private List<String> stacktrace;
}
