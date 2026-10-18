package com.example.ConnectService.Exception.Global;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.ConnectService.Exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFound(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            new ErrorMessage(ex.getMessage(), "NOT_FOUND")
        );
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFound(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorMessage(ex.getMessage(), "INVALID INPUT")
        );
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFound(IllegalStateException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ErrorMessage(ex.getMessage(), "Internal Error")
        );
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorMessage("UNKNOWN_ERROR", "An unexpected error occurred"));
    }
}

class ErrorMessage{
    public String message;
    public String status;
    public LocalDateTime timestamp;

    public  ErrorMessage(String message, String status){
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
