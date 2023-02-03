package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
