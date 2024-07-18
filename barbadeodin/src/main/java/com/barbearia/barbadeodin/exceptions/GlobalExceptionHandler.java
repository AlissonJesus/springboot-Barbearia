package com.barbearia.barbadeodin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = "Dados inválidos fornecidos";
        
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException() {
        String errorMessage = "Ocorreu um erro interno";
        
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
