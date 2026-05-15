package com.personal.project.repositories;

import com.personal.project.entity.Payment;
import com.personal.project.enums.PaymentMethod;
import com.personal.project.enums.PaymentStatus;
import com.personal.project.enums.PaymentTransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository
        extends JpaRepository<Payment, Integer> {

    Page<Payment>
    findByUserUserId(
            Integer userId,
            Pageable pageable
    );

    Page<Payment>
    findByUserUserIdAndPaymentMethod(
            Integer userId,
            PaymentMethod paymentMethod,
            Pageable pageable
    );

    Page<Payment>
    findByUserUserIdAndTransactionType(
            Integer userId,
            PaymentTransactionType transactionType,
            Pageable pageable
    );

    Page<Payment>
    findByUserUserIdAndPaymentStatus(
            Integer userId,
            PaymentStatus paymentStatus,
            Pageable pageable
    );

    Page<Payment>
    findByUserUserIdOrderByCreatedAtDesc(
            Integer userId,
            Pageable pageable
    );

    Page<Payment>
    findByUserUserIdOrderByCreatedAtAsc(
            Integer userId,
            Pageable pageable
    );

    Page<Payment>
    findByUserUserIdOrderByAmountDesc(
            Integer userId,
            Pageable pageable
    );

    Page<Payment>
    findByUserUserIdOrderByAmountAsc(
            Integer userId,
            Pageable pageable
    );
}