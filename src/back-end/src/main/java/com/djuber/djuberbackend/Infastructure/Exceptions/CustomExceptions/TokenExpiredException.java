package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message) {super(message);}
}
