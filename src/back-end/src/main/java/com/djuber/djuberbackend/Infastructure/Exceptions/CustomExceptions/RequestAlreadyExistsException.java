package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class RequestAlreadyExistsException extends RuntimeException{
    public RequestAlreadyExistsException(String message) {super(message);}
}
