package edu.java.scrapper.api.exception;

public class IncorrectRequestParamsException extends RuntimeException {
    public IncorrectRequestParamsException(String message) {
        super(message);
    }
}
