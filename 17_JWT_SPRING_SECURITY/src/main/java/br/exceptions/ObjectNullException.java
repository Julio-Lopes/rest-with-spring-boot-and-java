package br.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectNullException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ObjectNullException() {
        super("It is allowed to persist a null object!");
    }
    
    public ObjectNullException(String ex) {
        super(ex);
    }
}
