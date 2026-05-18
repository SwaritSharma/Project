package com.personal.project.service.impl;

import com.personal.project.service.PaymentService;

import com.personal.project.constants.PaymentConstants;
import com.personal.project.entity.Payment;
import com.personal.project.entity.User;
import com.personal.project.mapper.PaymentMapper;
import com.personal.project.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentRepository
            paymentRepository;

    private final PaymentMapper
            paymentMapper;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            PaymentMapper paymentMapper
    ) {

        this.paymentRepository =
                paymentRepository;

        this.paymentMapper =
                paymentMapper;
    }

    @Override
    public Payment createWalletCreditEntry(
            User user,
            BigDecimal amount,
            String paymentMethod,
            String paymentStatus
    ) {

        Payment payment = paymentMapper.toEntity(
                user,
                amount,
                paymentMethod,
                PaymentConstants.CREDITED_TO_WALLET,
                paymentStatus,
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

        Payment payment = paymentMapper.toEntity(
                user,
                amount,
                paymentMethod,
                PaymentConstants.DEBITED_FROM_WALLET,
                paymentStatus,
                LocalDateTime.now()
        );

        return paymentRepository
                .save(payment);
    }
}
