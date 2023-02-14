package com.itzroma.astrocornerapi.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record HttpExceptionResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
        LocalDateTime timestamp,
        int statusCode,
        String reasonPhrase,
        String message
) {
    public HttpExceptionResponse(HttpStatus httpStatus, String message) {
        this(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }
}
