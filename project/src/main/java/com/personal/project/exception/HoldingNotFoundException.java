package com.personal.project.exception;

public class HoldingNotFoundException
        extends RuntimeException {

    public HoldingNotFoundException(
            String message
    ) {

        super(message);
    }
}