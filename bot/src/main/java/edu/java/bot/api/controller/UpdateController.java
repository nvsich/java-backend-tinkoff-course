package edu.java.bot.api.controller;

import edu.java.bot.api.dto.request.LinkUpdateRequest;
import edu.java.bot.api.dto.response.ApiErrorResponse;
import edu.java.bot.service.processor.LinkUpdateProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/updates")
public class UpdateController {

    private LinkUpdateProcessor linkUpdateProcessor;

    @PostMapping
    @Operation(summary = "Send update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Update has been processed"),
        @ApiResponse(responseCode = "400", description = "Incorrect request params",
                     content = {@Content(schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    public ResponseEntity<Void> sendUpdate(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        linkUpdateProcessor.process(linkUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
