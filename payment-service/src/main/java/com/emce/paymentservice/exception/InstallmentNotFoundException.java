package com.emce.paymentservice.exception;

public class InstallmentNotFoundException extends RuntimeException {
    public InstallmentNotFoundException(String message) {
        super(message);
    }
}