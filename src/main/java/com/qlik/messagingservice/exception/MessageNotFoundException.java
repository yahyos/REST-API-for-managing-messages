package com.qlik.messagingservice.exception;


public class MessageNotFoundException extends ResourceNotFoundException {

    public MessageNotFoundException(Long id) {
        super(id, "Could not find message with id: " + id);
    }

}