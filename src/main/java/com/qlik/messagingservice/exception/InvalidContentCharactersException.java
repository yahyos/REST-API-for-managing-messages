package com.qlik.messagingservice.exception;

public class InvalidContentCharactersException extends BadRequestException {
    public InvalidContentCharactersException() {
        super("Special characters are not allowed!");
    }
}
