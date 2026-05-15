package com.personal.project.exception;

public class UnauthorizedHoldingAccessException
        extends RuntimeException {

    public UnauthorizedHoldingAccessException(
            String message
    ) {

        super(message);
    }
}