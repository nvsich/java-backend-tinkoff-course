package edu.java.scrapper.controller;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.DeleteLinkRequest;
import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinkResponse;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.service.processor.LinksProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class LinksController {

    private LinksProcessor linksProcessor;

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

        List<Link> listLink = linksProcessor.getAllLinksForChat(tgChatId);

        List<LinkResponse> listLinkResponse = listLink.stream()
            .map(link -> LinkResponse.builder()
                .url(link.getUrl())
                .id(link.getId())
                .build())
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            ListLinkResponse
                .builder()
                .links(listLinkResponse)
                .size(listLinkResponse.size())
                .build()
        );
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

        Link link = linksProcessor.addLinkToChat(tgChatId, addLinkRequest.getUrl());

        return ResponseEntity.ok(
            LinkResponse
                .builder()
                .url(link.getUrl())
                .id(link.getId())
                .build()
        );
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
        @RequestBody DeleteLinkRequest deleteLinkRequest
    ) {
        log.info("ScrapperApiController#deleteLinkForChat: " + deleteLinkRequest);

        Link link = linksProcessor.deleteLinkForChat(tgChatId, deleteLinkRequest.getUrl());

        return ResponseEntity.ok(
            LinkResponse
                .builder()
                .url(link.getUrl())
                .id(link.getId())
                .build()
        );
    }
}
