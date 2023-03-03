package com.itzroma.astrocornerapi.exception.handler;

import com.itzroma.astrocornerapi.exception.*;
import com.itzroma.astrocornerapi.model.dto.HttpExceptionResponseDto;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerResource implements ErrorController {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpExceptionResponseDto> badCredentialsException(BadCredentialsException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<HttpExceptionResponseDto> emailTakenException(EmailTakenException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpExceptionResponseDto> entityNotFoundException(EntityNotFoundException ex) {
        return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EVTConfirmedException.class)
    public ResponseEntity<HttpExceptionResponseDto> evtConfirmedException(EVTConfirmedException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EVTExpiredException.class)
    public ResponseEntity<HttpExceptionResponseDto> evtExpiredException(EVTExpiredException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ReAuthenticationRequiredException.class)
    public ResponseEntity<HttpExceptionResponseDto> reAuthenticationRequiredException(ReAuthenticationRequiredException ex) {
        return createResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpExceptionResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Validation error")));
        return createResponse(HttpStatus.BAD_REQUEST, errors.toString());
    }

    private ResponseEntity<HttpExceptionResponseDto> createResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpExceptionResponseDto(httpStatus, message), httpStatus);
    }
}
