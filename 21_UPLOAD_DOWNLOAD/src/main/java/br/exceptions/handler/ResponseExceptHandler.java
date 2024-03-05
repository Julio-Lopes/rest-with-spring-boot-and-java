package br.exceptions.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.exceptions.ExceptionResponse;
import br.exceptions.InvalidJwtAuthen;
import br.exceptions.NotfoundException;
import br.exceptions.ObjectNullException;

@ControllerAdvice
@RestController
public class ResponseExceptHandler extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse
                (new Date(), ex.getMessage(), request.getDescription(false));
        
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotfoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse
                (new Date(), ex.getMessage(), request.getDescription(false));
        
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectNullException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse
                (new Date(), ex.getMessage(), request.getDescription(false));
        
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJwtAuthen.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthen (Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse
                (new Date(), ex.getMessage(), request.getDescription(false));
        
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }
}