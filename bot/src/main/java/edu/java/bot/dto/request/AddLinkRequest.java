package edu.java.bot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddLinkRequest {
    private String url;
}
