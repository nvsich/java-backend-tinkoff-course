package edu.java.scrapper.api.controller;

import edu.java.scrapper.api.dto.request.AddLinkRequest;
import edu.java.scrapper.api.dto.request.RemoveLinkRequest;
import edu.java.scrapper.api.dto.response.ApiErrorResponse;
import edu.java.scrapper.api.dto.response.LinkResponse;
import edu.java.scrapper.api.dto.response.ListLinkResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/links")
public class LinksController {
    @GetMapping
    @Operation(summary = "Get all links for given chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Links have been successfully taken",
                     content = {@Content(schema = @Schema(implementation = ListLinkResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Incorrect request params",
                     content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    public ResponseEntity<ListLinkResponse> getAllLinksForChat(@RequestHeader(value = "Tg-Chat-Id") Long tgChatId) {
        log.info("ScrapperApiController#getAllLinksForChat: " + tgChatId);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(summary = "Add link for tracking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Links has been successfully added",
                     content = {@Content(schema = @Schema(implementation = LinkResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Incorrect request params",
                     content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    public ResponseEntity<LinkResponse> addLinkToChat(
        @RequestHeader(value = "Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        log.info("ScrapperApiController#addLinkToChat: " + addLinkRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete link from chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Link has been deleted"),
        @ApiResponse(responseCode = "400", description = "Incorrect request params",
                     content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Link not found",
                     content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    public ResponseEntity<LinkResponse> deleteLinkForChat(
        @RequestHeader(value = "Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        log.info("ScrapperApiController#deleteLinkForChat: " + removeLinkRequest);

        return ResponseEntity.ok().build();
    }
}
