package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
