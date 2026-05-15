package com.personal.project.controllers;

import com.personal.project.dtos.AddMoneyToWalletRequestDto;
import com.personal.project.dtos.PaymentResponseDto;
import com.personal.project.dtos.TransactionHistoryResponseDto;
import com.personal.project.enums.PaymentMethod;
import com.personal.project.enums.PaymentStatus;
import com.personal.project.enums.PaymentTransactionType;
import com.personal.project.enums.TransactionStatus;
import com.personal.project.enums.TransactionType;
import com.personal.project.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{userId}/wallet/add-money")
    public ResponseEntity<PaymentResponseDto>
    addMoneyToWallet(
            @PathVariable
            Integer userId,

            @Valid
            @RequestBody
            AddMoneyToWalletRequestDto dto
    ) {

        PaymentResponseDto response =
                paymentService.addMoneyToWallet(
                        userId,
                        dto
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<PaymentResponseDto>>
    getUserPayments(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService.getUserPayments(
                        userId,
                        pageable
                )
        );
    }

    @GetMapping("/{userId}/method/{paymentMethod}")
    public ResponseEntity<Page<PaymentResponseDto>>
    getUserPaymentsByMethod(
            @PathVariable
            Integer userId,

            @PathVariable
            PaymentMethod paymentMethod,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService.getUserPaymentsByMethod(
                        userId,
                        paymentMethod,
                        pageable
                )
        );
    }

    @GetMapping("/{userId}/transaction-type/{transactionType}")
    public ResponseEntity<Page<PaymentResponseDto>>
    getUserPaymentsByTransactionType(
            @PathVariable
            Integer userId,

            @PathVariable
            PaymentTransactionType transactionType,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService
                        .getUserPaymentsByTransactionType(
                                userId,
                                transactionType,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/status/{paymentStatus}")
    public ResponseEntity<Page<PaymentResponseDto>>
    getUserPaymentsByStatus(
            @PathVariable
            Integer userId,

            @PathVariable
            PaymentStatus paymentStatus,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService.getUserPaymentsByStatus(
                        userId,
                        paymentStatus,
                        pageable
                )
        );
    }

    @GetMapping("/{userId}/sort/created-at-desc")
    public ResponseEntity<Page<PaymentResponseDto>>
    getUserPaymentsSortedByCreatedAtDesc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService
                        .getUserPaymentsSortedByCreatedAtDesc(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/sort/created-at-asc")
    public ResponseEntity<Page<PaymentResponseDto>>
    getUserPaymentsSortedByCreatedAtAsc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService
                        .getUserPaymentsSortedByCreatedAtAsc(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/sort/amount-desc")
    public ResponseEntity<Page<PaymentResponseDto>>
    getUserPaymentsSortedByAmountDesc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService
                        .getUserPaymentsSortedByAmountDesc(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/sort/amount-asc")
    public ResponseEntity<Page<PaymentResponseDto>>
    getUserPaymentsSortedByAmountAsc(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService
                        .getUserPaymentsSortedByAmountAsc(
                                userId,
                                pageable
                        )
        );
    }

    @GetMapping("/{userId}/transactions")
    public ResponseEntity<Page<TransactionHistoryResponseDto>>
    getUserTransactions(
            @PathVariable
            Integer userId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService.getUserTransactions(
                        userId,
                        pageable
                )
        );
    }

    @GetMapping("/{userId}/transactions/type/{transactionType}")
    public ResponseEntity<Page<TransactionHistoryResponseDto>>
    getUserTransactionsByType(
            @PathVariable
            Integer userId,

            @PathVariable
            TransactionType transactionType,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService.getUserTransactionsByType(
                        userId,
                        transactionType,
                        pageable
                )
        );
    }

    @GetMapping("/{userId}/transactions/status/{transactionStatus}")
    public ResponseEntity<Page<TransactionHistoryResponseDto>>
    getUserTransactionsByStatus(
            @PathVariable
            Integer userId,

            @PathVariable
            TransactionStatus transactionStatus,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                paymentService.getUserTransactionsByStatus(
                        userId,
                        transactionStatus,
                        pageable
                )
        );
    }
}