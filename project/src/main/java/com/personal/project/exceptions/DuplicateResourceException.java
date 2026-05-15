package com.personal.project.exceptions;

public class DuplicateResourceException
        extends RuntimeException {

    public DuplicateResourceException(
            String message
    ) {
        super(message);
    }
}