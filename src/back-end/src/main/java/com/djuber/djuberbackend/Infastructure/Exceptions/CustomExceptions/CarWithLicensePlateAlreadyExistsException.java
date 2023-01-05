package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class CarWithLicensePlateAlreadyExistsException  extends RuntimeException{
    public CarWithLicensePlateAlreadyExistsException(String message){super(message);}
}
