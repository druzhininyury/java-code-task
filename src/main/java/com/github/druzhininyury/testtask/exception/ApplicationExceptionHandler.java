package com.github.druzhininyury.testtask.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
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

}
