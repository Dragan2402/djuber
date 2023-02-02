package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class RideNotActiveException extends RuntimeException{
    public RideNotActiveException(String msg) {super(msg);}
}
