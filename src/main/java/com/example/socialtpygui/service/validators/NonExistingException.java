package com.example.socialtpygui.service.validators;


public class NonExistingException extends RuntimeException{

    public NonExistingException(String msj){
        super(msj);
    }
}
