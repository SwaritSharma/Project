package com.personal.project.services;

import com.personal.project.dtos.AddMoneyToWalletRequestDto;
import com.personal.project.dtos.PaymentResponseDto;
import com.personal.project.dtos.TransactionHistoryResponseDto;
import com.personal.project.enums.PaymentMethod;
import com.personal.project.enums.PaymentStatus;
import com.personal.project.enums.PaymentTransactionType;
import com.personal.project.enums.TransactionStatus;
import com.personal.project.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    PaymentResponseDto addMoneyToWallet(
            Integer userId,
            AddMoneyToWalletRequestDto dto
    );

    Page<TransactionHistoryResponseDto>
    getUserTransactions(
            Integer userId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getUserTransactionsByType(
            Integer userId,
            TransactionType transactionType,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getUserTransactionsByStatus(
            Integer userId,
            TransactionStatus transactionStatus,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getUserTransactionsSortedByCreatedAtDesc(
            Integer userId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getUserTransactionsSortedByCreatedAtAsc(
            Integer userId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getUserTransactionsSortedByAmountDesc(
            Integer userId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getUserTransactionsSortedByAmountAsc(
            Integer userId,
            Pageable pageable
    );

    Page<PaymentResponseDto>
    getUserPayments(
            Integer userId,
            Pageable pageable
    );

    Page<PaymentResponseDto>
    getUserPaymentsByMethod(
            Integer userId,
            PaymentMethod paymentMethod,
            Pageable pageable
    );

    Page<PaymentResponseDto>
    getUserPaymentsByTransactionType(
            Integer userId,
            PaymentTransactionType transactionType,
            Pageable pageable
    );

    Page<PaymentResponseDto>
    getUserPaymentsByStatus(
            Integer userId,
            PaymentStatus paymentStatus,
            Pageable pageable
    );

    Page<PaymentResponseDto>
    getUserPaymentsSortedByCreatedAtDesc(
            Integer userId,
            Pageable pageable
    );

    Page<PaymentResponseDto>
    getUserPaymentsSortedByCreatedAtAsc(
            Integer userId,
            Pageable pageable
    );

    Page<PaymentResponseDto>
    getUserPaymentsSortedByAmountDesc(
            Integer userId,
            Pageable pageable
    );

    Page<PaymentResponseDto>
    getUserPaymentsSortedByAmountAsc(
            Integer userId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getVendorTransactions(
            Integer vendorId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getVendorTransactionsByType(
            Integer vendorId,
            TransactionType transactionType,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getVendorTransactionsByStatus(
            Integer vendorId,
            TransactionStatus transactionStatus,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getVendorTransactionsByBranch(
            Integer branchId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getVendorTransactionsSortedByCreatedAtDesc(
            Integer vendorId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getVendorTransactionsSortedByCreatedAtAsc(
            Integer vendorId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getVendorTransactionsSortedByAmountDesc(
            Integer vendorId,
            Pageable pageable
    );

    Page<TransactionHistoryResponseDto>
    getVendorTransactionsSortedByAmountAsc(
            Integer vendorId,
            Pageable pageable
    );
}