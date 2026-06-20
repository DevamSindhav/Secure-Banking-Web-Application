package com.bank.unitedbank.exception;

public class ActiveAccountException extends RuntimeException{
    public ActiveAccountException(String message){
        super(message);
    }
}
