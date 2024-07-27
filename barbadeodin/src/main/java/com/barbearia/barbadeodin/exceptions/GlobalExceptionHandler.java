package com.barbearia.barbadeodin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleError400(MethodArgumentNotValidException ex) {
        String errorMessage = "Dados fornecidos inválidos";
        
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleError404(EntityNotFoundException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleError404(EmailAlreadyExistsException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    } 
    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException() {
        String errorMessage = "Ocorreu um erro interno";     
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
