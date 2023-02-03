package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class InvalidReservationStartException extends RuntimeException{
    public InvalidReservationStartException(String msg){super(msg);}
}
