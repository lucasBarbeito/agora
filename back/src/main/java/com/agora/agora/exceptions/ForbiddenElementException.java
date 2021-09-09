package com.agora.agora.exceptions;

public class ForbiddenElementException extends RuntimeException{
    public ForbiddenElementException(String message){
        super(message);
    }
}