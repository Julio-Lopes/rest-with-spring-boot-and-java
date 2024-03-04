package br.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthen extends AuthenticationException{
    private static final long serialVersionUID = 1L;

    public InvalidJwtAuthen(String ex) {
        super(ex);
    }
}