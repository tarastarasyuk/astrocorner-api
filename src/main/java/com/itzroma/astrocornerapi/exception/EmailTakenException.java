package com.itzroma.astrocornerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailTakenException extends RuntimeException {
    public EmailTakenException() {
        super("Email is already taken");
    }
}
