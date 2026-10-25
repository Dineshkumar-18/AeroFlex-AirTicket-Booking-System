package com.aeroflex.common.exception;

public class InvalidStatusTransitionException extends BusinessException{
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
