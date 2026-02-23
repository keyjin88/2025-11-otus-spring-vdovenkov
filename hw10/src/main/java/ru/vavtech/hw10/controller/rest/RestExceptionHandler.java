package ru.vavtech.hw10.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vavtech.hw10.exception.NotFoundException;
import ru.vavtech.hw10.model.dto.ErrorDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorDto.builder()
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                response.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(ErrorDto.builder()
                        .errors(response).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal server error");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDto.builder().message(ex.getMessage()).build());
    }
} 