package com.personal.project.service;

import com.personal.project.entity.Payment;
import com.personal.project.entity.User;

import java.math.BigDecimal;

public interface PaymentService {

    Payment createWalletCreditEntry(
            User user,
            BigDecimal amount,
            String paymentMethod,
            String paymentStatus
    );

    Payment createWalletDebitEntry(
            User user,
            BigDecimal amount,
            String paymentMethod,
            String paymentStatus
    );
}