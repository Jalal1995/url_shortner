package com.example.url_shortner.exception;

public class InvalidLinkException extends RuntimeException {
    public InvalidLinkException(String message) {
        super(message);
    }
}
