package com.ndt.be_stepupsneaker.infrastructure.exception;

public class AccessException extends RuntimeException{
    public AccessException(String message) {
        super(message);
    }
}
