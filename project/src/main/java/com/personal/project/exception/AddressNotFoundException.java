package com.personal.project.exception;

public class AddressNotFoundException
        extends RuntimeException {

    public AddressNotFoundException(
            String message
    ) {

        super(message);
    }
}