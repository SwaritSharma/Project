package com.personal.project.service;

import com.personal.project.constants.PaymentConstants;
import com.personal.project.entity.Payment;
import com.personal.project.entity.User;
import com.personal.project.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentRepository
            paymentRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository
    ) {

        this.paymentRepository =
                paymentRepository;
    }

    @Override
    public Payment createWalletCreditEntry(
            User user,
            BigDecimal amount,
            String paymentMethod,
            String paymentStatus
    ) {

        Payment payment = new Payment();

        payment.setUser(user);

        payment.setAmount(amount);

        payment.setPaymentMethod(
                paymentMethod
        );

        payment.setTransactionType(
                PaymentConstants
                        .CREDITED_TO_WALLET
        );

        payment.setPaymentStatus(
                paymentStatus
        );

        payment.setCreatedAt(
                LocalDateTime.now()
        );

        return paymentRepository
                .save(payment);
    }

    @Override
    public Payment createWalletDebitEntry(
            User user,
            BigDecimal amount,
            String paymentMethod,
            String paymentStatus
    ) {

        Payment payment = new Payment();

        payment.setUser(user);

        payment.setAmount(amount);

        payment.setPaymentMethod(
                paymentMethod
        );

        payment.setTransactionType(
                PaymentConstants
                        .DEBITED_FROM_WALLET
        );

        payment.setPaymentStatus(
                paymentStatus
        );

        payment.setCreatedAt(
                LocalDateTime.now()
        );

        return paymentRepository
                .save(payment);
    }
}