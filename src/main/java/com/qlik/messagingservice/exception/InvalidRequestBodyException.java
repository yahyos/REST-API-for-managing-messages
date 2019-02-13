package com.qlik.messagingservice.exception;

public class InvalidRequestBodyException extends BadRequestException {
    public InvalidRequestBodyException() {
        super("Request body should be a json containing only one key: 'content'");
    }
}
