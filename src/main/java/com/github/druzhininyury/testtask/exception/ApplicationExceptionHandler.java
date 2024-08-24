package com.github.druzhininyury.testtask.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleEntityNotFoundException(EntityNotFoundException exception) {
        Error error = Error.builder()
                .type(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();

        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleEntityNotFoundException(IllegalOperationException exception) {
        Error error = Error.builder()
                .type(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();

        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleValidationException(MethodArgumentNotValidException exception) {
        Error error = Error.builder()
                .type(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();

        return error;
    }

}
