package com.personal.project.exceptions;

public class InsufficientGoldQuantityException
        extends RuntimeException {

    public InsufficientGoldQuantityException(
            String message
    ) {
        super(message);
    }
}