package com.seyed.ali.authenticationservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class AuthServiceHandlerAdvice {

    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<?> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException e) {
        return ResponseEntity.ok(Map.of(
                "code", FORBIDDEN,
                "message", "No permission.",
                "server_message", e.getMessage()
        ));
    }

}
