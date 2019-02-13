package com.qlik.messagingservice.exception;

public class InvalidContentLengthException extends BadRequestException {
    public InvalidContentLengthException() {
        super("Content length should be between 1 and 500 characters.");
    }
}
