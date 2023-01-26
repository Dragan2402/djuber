package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class DriverReachedLimitOfActiveHoursException extends RuntimeException{
    public DriverReachedLimitOfActiveHoursException(String msg) {super(msg);}
}
