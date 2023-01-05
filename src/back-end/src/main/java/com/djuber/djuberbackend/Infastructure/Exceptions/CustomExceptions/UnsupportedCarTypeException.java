package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class UnsupportedCarTypeException extends RuntimeException{
    public UnsupportedCarTypeException(String message) {
        super(message);
    }
}
