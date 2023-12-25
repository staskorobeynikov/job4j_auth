package ru.job4j.persons.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(fieldError -> Map.of(
                                fieldError.getField(),
                                String.format(
                                        "%s. Actual value: %s",
                                        fieldError.getDefaultMessage(),
                                        fieldError.getRejectedValue()
                                )
                        ))
                        .collect(Collectors.toList())
        );
    }
}
