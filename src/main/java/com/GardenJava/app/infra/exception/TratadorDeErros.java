package com.GardenJava.app.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }


    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity tratarErroRegraDeNegocio(Exception ex) {

        return ResponseEntity.badRequest().body(new DadosErroSimples(ex.getMessage()));
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity tratarErro403() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DadosErroSimples("Acesso negado"));
    }


    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }


    private record DadosErroSimples(String mensagem) {}
}