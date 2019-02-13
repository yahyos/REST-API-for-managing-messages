package com.qlik.messagingservice.exception;

public class MissingContentException extends BadRequestException {
    public MissingContentException() {
        super("Please provide a valid content for your message.");
    }
}
