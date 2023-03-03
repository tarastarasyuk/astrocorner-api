package com.itzroma.astrocornerapi.security.exceptionhandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itzroma.astrocornerapi.model.dto.HttpExceptionResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream outputStream = response.getOutputStream();
        new ObjectMapper().writeValue(outputStream, new HttpExceptionResponseDto(
                HttpStatus.UNAUTHORIZED, authException.getMessage()
        ));
        outputStream.flush();
    }
}
