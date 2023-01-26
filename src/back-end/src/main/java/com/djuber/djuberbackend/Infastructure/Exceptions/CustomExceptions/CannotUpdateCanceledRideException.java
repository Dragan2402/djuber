package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class CannotUpdateCanceledRideException extends RuntimeException{
    public CannotUpdateCanceledRideException(String msg) {super(msg);}
}
