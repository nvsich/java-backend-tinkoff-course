package edu.java.bot.api.exception;

import edu.java.bot.api.dto.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BotApiExceptionHandler {

    @ExceptionHandler(InvalidLinkUpdateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidLinkUpdateException(InvalidLinkUpdateException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
            .description(ex.getMessage())
            .code("400")
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .stacktrace(ex.getStackTrace())
            .build();

        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }
}