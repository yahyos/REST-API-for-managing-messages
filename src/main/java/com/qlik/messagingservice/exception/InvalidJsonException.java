package com.qlik.messagingservice.exception;

public class InvalidJsonException extends BadRequestException {
    public InvalidJsonException() {
        super("Please provide valid json in your request body.");
    }
}
