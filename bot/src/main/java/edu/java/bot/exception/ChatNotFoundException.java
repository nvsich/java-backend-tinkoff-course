package edu.java.bot.exception;

public class ChatNotFoundException extends RuntimeException {

    public ChatNotFoundException() {

    }

    public ChatNotFoundException(String message) {
        super(message);
    }
}
