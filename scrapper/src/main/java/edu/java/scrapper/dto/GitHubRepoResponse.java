package edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubRepoResponse(
    @JsonProperty("id")
    Long id,

    @JsonProperty("name")
    String name,

    @JsonProperty("full_name")
    String fullName,

    @JsonProperty("owner")
    Owner owner,

    @JsonProperty("html_url")
    String htmlUrl,

    @JsonProperty("url")
    String url,

    @JsonProperty("description")
    String description,

    @JsonProperty("created_at")
    OffsetDateTime createdAt,

    @JsonProperty("updated_at")
    OffsetDateTime updatedAt

) {
    public record Owner(
        @JsonProperty("login")
        String login,

        @JsonProperty("id")
        Long id,

        @JsonProperty("url")
        String url,

        @JsonProperty("html_url")
        String htmlUrl

    ) {
    }
}
