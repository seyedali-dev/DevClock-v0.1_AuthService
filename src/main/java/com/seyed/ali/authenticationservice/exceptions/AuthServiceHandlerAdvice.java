package com.seyed.ali.authenticationservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class AuthServiceHandlerAdvice {

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(FORBIDDEN).body(Map.of(
                "code", FORBIDDEN.toString(),
                "message", "No permission.",
                "server_message", e.getMessage()
        ));
    }

}
