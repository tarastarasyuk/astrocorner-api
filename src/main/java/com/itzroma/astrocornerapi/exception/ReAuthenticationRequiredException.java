package com.itzroma.astrocornerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ReAuthenticationRequiredException extends RuntimeException {
    public ReAuthenticationRequiredException() {
        super("Perform re-authentication");
    }
}
