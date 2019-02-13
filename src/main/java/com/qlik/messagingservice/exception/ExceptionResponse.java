package com.qlik.messagingservice.exception;

public class ExceptionResponse {
    private String code;
    private String message;

    public ExceptionResponse() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String errorCode) {
        this.code = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String errorMessage) {
        this.message = errorMessage;
    }
}
