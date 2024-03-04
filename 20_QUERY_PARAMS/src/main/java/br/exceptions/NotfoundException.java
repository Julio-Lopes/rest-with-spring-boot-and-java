package br.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotfoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotfoundException(String ex) {
        super(ex);
    }
}
