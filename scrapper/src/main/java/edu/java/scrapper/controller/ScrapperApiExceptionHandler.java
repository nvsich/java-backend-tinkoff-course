package edu.java.scrapper.controller;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.exception.ChatIsRegisteredException;
import edu.java.scrapper.exception.ChatNotFoundException;
import edu.java.scrapper.exception.LinkDomainNotSupportedException;
import edu.java.scrapper.exception.LinkIsNotReachableException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.LinkSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ScrapperApiExceptionHandler {

    @ExceptionHandler(value = {
        ChatIsRegisteredException.class,
        LinkIsNotReachableException.class,
        LinkSyntaxException.class,
        LinkDomainNotSupportedException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
            .description(ex.getMessage())
            .code(HttpStatus.BAD_REQUEST.toString())
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .stacktrace(ex.getStackTrace())
            .build();

        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
        LinkNotFoundException.class,
        ChatNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFound(Exception ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
            .description(ex.getMessage())
            .code(HttpStatus.NOT_FOUND.toString())
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .stacktrace(ex.getStackTrace())
            .build();

        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }
}
