package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
