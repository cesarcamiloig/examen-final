package com.examen.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        String errorJson = String.format(
            "{\"status\": \"error\", \"message\": \"%s\", \"data\": null}",
            ex.getMessage()
        );
        return new ResponseEntity<>(errorJson, ex.getStatus());
    }
}