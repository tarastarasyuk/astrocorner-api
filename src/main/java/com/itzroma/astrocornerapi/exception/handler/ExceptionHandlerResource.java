package com.itzroma.astrocornerapi.exception.handler;

import com.itzroma.astrocornerapi.exception.BadCredentialsException;
import com.itzroma.astrocornerapi.exception.EmailTakenException;
import com.itzroma.astrocornerapi.exception.EntityNotFoundException;
import com.itzroma.astrocornerapi.exception.ReAuthenticationRequiredException;
import com.itzroma.astrocornerapi.model.dto.HttpExceptionResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerResource implements ErrorController {
    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<HttpExceptionResponse> emailTakenException(EmailTakenException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpExceptionResponse> entityNotFoundException(EntityNotFoundException ex) {
        return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ReAuthenticationRequiredException.class)
    public ResponseEntity<HttpExceptionResponse> reAuthenticationRequiredException(ReAuthenticationRequiredException ex) {
        return createResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpExceptionResponse> badCredentialsException(BadCredentialsException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<HttpExceptionResponse> createResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpExceptionResponse(httpStatus, message), httpStatus);
    }
}
