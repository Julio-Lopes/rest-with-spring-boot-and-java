package br.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNDException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public MyFileNDException(String ex) {
        super(ex);
    }

    public MyFileNDException(String ex, Throwable cause) {
        super(ex, cause);
    }
}