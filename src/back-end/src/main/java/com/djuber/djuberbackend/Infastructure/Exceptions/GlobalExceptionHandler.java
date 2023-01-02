package com.djuber.djuberbackend.Infastructure.Exceptions;

import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.EmailAlreadyExistsException;
import com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(),HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmailAlreadyExistsException.class})
    public ResponseEntity<ErrorObject> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, WebRequest request){
        return new ResponseEntity<>(getErrorObject(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorObject>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<ErrorObject> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.add(getErrorObject(error.getDefaultMessage(), HttpStatus.BAD_REQUEST));
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private static ErrorObject getErrorObject(String message, HttpStatus status){
        ErrorObject error= new ErrorObject();
        error.setStatusCode(status.value());
        error.setMessage(message);
        error.setTimeStamp(new Date());
        return error;
    }

}
