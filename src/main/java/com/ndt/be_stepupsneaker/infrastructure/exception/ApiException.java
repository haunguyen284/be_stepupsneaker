package com.ndt.be_stepupsneaker.infrastructure.exception;

public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}
