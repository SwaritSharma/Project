package com.personal.project.exception;

public class InsufficientHoldingQuantityException
        extends RuntimeException {

    public InsufficientHoldingQuantityException(
            String message
    ) {

        super(message);
    }
}