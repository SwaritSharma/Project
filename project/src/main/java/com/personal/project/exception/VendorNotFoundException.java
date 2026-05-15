package com.personal.project.exception;

public class VendorNotFoundException
        extends RuntimeException {

    public VendorNotFoundException(
            String message
    ) {

        super(message);
    }
}