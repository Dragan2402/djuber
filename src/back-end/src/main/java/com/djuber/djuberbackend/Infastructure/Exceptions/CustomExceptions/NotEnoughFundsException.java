package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException(String message) {
        super(message);
    }
}
