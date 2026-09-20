package com.aeroflex.identityservice.exception;

public class InvalidPasswordResetTokenException
        extends RuntimeException {

    public InvalidPasswordResetTokenException(String message) {
        super(message);
    }
}
