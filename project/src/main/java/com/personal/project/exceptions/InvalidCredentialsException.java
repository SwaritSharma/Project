package com.personal.project.exceptions;

public class InvalidCredentialsException
        extends RuntimeException {

    public InvalidCredentialsException(
            String message
    ) {
        super(message);
    }
}