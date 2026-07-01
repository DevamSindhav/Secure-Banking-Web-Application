package com.bank.unitedbank.exception;

public class TransferToSelfException extends RuntimeException {
    public TransferToSelfException(String message) {
        super(message);
    }
}
