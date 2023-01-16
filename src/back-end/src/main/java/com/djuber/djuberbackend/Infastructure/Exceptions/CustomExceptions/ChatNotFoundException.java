package com.djuber.djuberbackend.Infastructure.Exceptions.CustomExceptions;

public class ChatNotFoundException extends RuntimeException{
    public ChatNotFoundException(String message){super(message);}
}
