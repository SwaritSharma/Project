package com.personal.project.service;

import com.personal.project.constants.PaymentConstants;
import com.personal.project.entity.Payment;
import com.personal.project.entity.User;
import com.personal.project.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository
            paymentRepository;

    @InjectMocks
    private PaymentServiceImpl
            paymentService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setUserId(1);
    }

    @Test
    void createWalletCreditEntry_ShouldCreateSuccessfully() {

        when(
                paymentRepository.save(
                        any(Payment.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );

        Payment payment =
                paymentService
                        .createWalletCreditEntry(
                                user,
                                new BigDecimal("5000"),
                                PaymentConstants.BANK_TRANSFER,
                                PaymentConstants.SUCCESS
                        );

        assertNotNull(payment);

        assertEquals(
                user,
                payment.getUser()
        );

        assertEquals(
                0,
                new BigDecimal("5000")
                        .compareTo(
                                payment.getAmount()
                        )
        );

        assertEquals(
                PaymentConstants.BANK_TRANSFER,
                payment.getPaymentMethod()
        );

        assertEquals(
                PaymentConstants.CREDITED_TO_WALLET,
                payment.getTransactionType()
        );

        assertEquals(
                PaymentConstants.SUCCESS,
                payment.getPaymentStatus()
        );

        assertNotNull(
                payment.getCreatedAt()
        );

        verify(paymentRepository)
                .save(
                        any(Payment.class)
                );
    }

    @Test
    void createWalletDebitEntry_ShouldCreateSuccessfully() {

        when(
                paymentRepository.save(
                        any(Payment.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );

        Payment payment =
                paymentService
                        .createWalletDebitEntry(
                                user,
                                new BigDecimal("3000"),
                                PaymentConstants.BANK_TRANSFER,
                                PaymentConstants.SUCCESS
                        );

        assertNotNull(payment);

        assertEquals(
                user,
                payment.getUser()
        );

        assertEquals(
                0,
                new BigDecimal("3000")
                        .compareTo(
                                payment.getAmount()
                        )
        );

        assertEquals(
                PaymentConstants.BANK_TRANSFER,
                payment.getPaymentMethod()
        );

        assertEquals(
                PaymentConstants.DEBITED_FROM_WALLET,
                payment.getTransactionType()
        );

        assertEquals(
                PaymentConstants.SUCCESS,
                payment.getPaymentStatus()
        );

        assertNotNull(
                payment.getCreatedAt()
        );

        verify(paymentRepository)
                .save(
                        any(Payment.class)
                );
    }
}