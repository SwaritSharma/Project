package com.personal.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiErrorResponse>
    buildErrorResponse(
            HttpStatus status,
            String message
    ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                        status.value(),
                        message,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                response,
                status
        );
    }

    @ExceptionHandler(
            UserNotFoundException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleUserNotFoundException(
            UserNotFoundException ex
    ) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            VendorNotFoundException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleVendorNotFoundException(
            VendorNotFoundException ex
    ) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            HoldingNotFoundException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleHoldingNotFoundException(
            HoldingNotFoundException ex
    ) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            AddressNotFoundException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleAddressNotFoundException(
            AddressNotFoundException ex
    ) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            InvalidQuantityException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleInvalidQuantityException(
            InvalidQuantityException ex
    ) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            InsufficientWalletBalanceException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleInsufficientWalletBalanceException(
            InsufficientWalletBalanceException ex
    ) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            InsufficientHoldingQuantityException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleInsufficientHoldingQuantityException(
            InsufficientHoldingQuantityException ex
    ) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            UnauthorizedHoldingAccessException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleUnauthorizedHoldingAccessException(
            UnauthorizedHoldingAccessException ex
    ) {

        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            BranchAllocationException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleBranchAllocationException(
            BranchAllocationException ex
    ) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        String message =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .findFirst()
                        .map(
                                error ->
                                        error.getDefaultMessage()
                        )
                        .orElse(
                                "Validation failed"
                        );

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    @ExceptionHandler(
            InvalidCredentialsException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleInvalidCredentialsException(
            InvalidCredentialsException ex
    ) {

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            Exception.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleGenericException(
            Exception ex
    ) {
        ex.printStackTrace();
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong"
        );
    }
}