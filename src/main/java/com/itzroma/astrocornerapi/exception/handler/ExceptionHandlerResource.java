package com.itzroma.astrocornerapi.exception.handler;

import com.itzroma.astrocornerapi.exception.*;
import com.itzroma.astrocornerapi.model.dto.HttpExceptionResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerResource implements ErrorController {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpExceptionResponse> badCredentialsException(BadCredentialsException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<HttpExceptionResponse> emailTakenException(EmailTakenException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpExceptionResponse> entityNotFoundException(EntityNotFoundException ex) {
        return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EVTConfirmedException.class)
    public ResponseEntity<HttpExceptionResponse> evtConfirmedException(EVTConfirmedException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EVTExpiredException.class)
    public ResponseEntity<HttpExceptionResponse> evtExpiredException(EVTExpiredException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ReAuthenticationRequiredException.class)
    public ResponseEntity<HttpExceptionResponse> reAuthenticationRequiredException(ReAuthenticationRequiredException ex) {
        return createResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    private ResponseEntity<HttpExceptionResponse> createResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpExceptionResponse(httpStatus, message), httpStatus);
    }
}
