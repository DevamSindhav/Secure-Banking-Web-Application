package com.bank.unitedbank.exception;

public class SameNewPasswordException extends RuntimeException{
    public SameNewPasswordException(String message){
        super(message);
    }
}
