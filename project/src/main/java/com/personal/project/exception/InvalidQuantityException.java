package com.personal.project.exception;

public class InvalidQuantityException
        extends RuntimeException {

    public InvalidQuantityException(
            String message
    ) {

        super(message);
    }
}