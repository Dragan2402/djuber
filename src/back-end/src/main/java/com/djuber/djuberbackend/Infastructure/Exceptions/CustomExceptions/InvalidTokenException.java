package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message) {super(message);}
}
