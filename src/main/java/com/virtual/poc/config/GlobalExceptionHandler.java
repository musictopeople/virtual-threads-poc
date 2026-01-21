package com.virtual.poc.config;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<String> handleValidationException(HandlerMethodValidationException e) {
    log.warn("Validation error", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            e.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ")));
  }

  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<String> handleConstraintViolation(DuplicateKeyException e) {
    log.warn("Constraint violation", e);
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict Request");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    log.error("Unhandled exception", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
  }
}
