package com.qlik.messagingservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlingController {

    //Custom exception handler for 404 errors
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, ExceptionResponse>> resourceNotFound(ResourceNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode("404 Not Found");
        response.setMessage(ex.getMessage());
        Map<String, ExceptionResponse> responseMap = new HashMap<>();
        responseMap.put("error", response);

        return new ResponseEntity<Map<String, ExceptionResponse>>(responseMap, HttpStatus.NOT_FOUND);
    }

    //Custom exception handler for 400 errors
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, ExceptionResponse>> badRequest(BadRequestException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode("400 Bad Request");
        response.setMessage(ex.getMessage());
        Map<String, ExceptionResponse> responseMap = new HashMap<>();
        responseMap.put("error", response);

        return new ResponseEntity<Map<String, ExceptionResponse>>(responseMap, HttpStatus.BAD_REQUEST);
    }
}







