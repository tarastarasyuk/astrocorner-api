package com.itzroma.astrocornerapi.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Date;

public record HttpExceptionResponseDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
        Date timestamp,
        int statusCode,
        String reason,
        String message
) {
    public HttpExceptionResponseDto(HttpStatus httpStatus, String message) {
        this(new Date(System.currentTimeMillis()), httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }
}
