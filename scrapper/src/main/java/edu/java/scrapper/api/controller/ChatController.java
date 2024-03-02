package edu.java.scrapper.api.controller;

import edu.java.scrapper.api.dto.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/tg-chat")
public class ChatController {
    @PostMapping("/{id}")
    @Operation(summary = "Register chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chat has been registered"),
        @ApiResponse(responseCode = "400", description = "Incorrect request params",
                     content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    public ResponseEntity<Void> registerChat(@PathVariable Long id) {
        log.info("ScrapperApiController#registerChat: " + id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chat has been deleted"),
        @ApiResponse(responseCode = "400", description = "Incorrect request params",
                     content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Chat not found",
                     content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        log.info("ScrapperApiController#deleteChat: " + id);

        return ResponseEntity.ok().build();
    }
}
