package com.personal.project.service;

import com.personal.project.constants.PaymentConstants;
import com.personal.project.dto.WalletTopupRequest;
import com.personal.project.entity.User;
import com.personal.project.exception.InvalidQuantityException;
import com.personal.project.exception.UserNotFoundException;
import com.personal.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl
        implements WalletService {

    private final UserRepository
            userRepository;

    private final PaymentService
            paymentService;

    public WalletServiceImpl(
            UserRepository userRepository,
            PaymentService paymentService
    ) {

        this.userRepository =
                userRepository;

        this.paymentService =
                paymentService;
    }

    @Override
    @Transactional
    public User topupWallet(
            WalletTopupRequest request
    ) {

        if (
                request.getAmount()
                        .compareTo(BigDecimal.ZERO)
                        <= 0
        ) {

            throw new InvalidQuantityException(
                    "Amount must be greater than 0"
            );
        }

        User user =
                userRepository
                        .findById(
                                request.getUserId()
                        )
                        .orElseThrow(
                                () ->
                                        new UserNotFoundException(
                                                "User not found"
                                        )
                        );

        BigDecimal updatedBalance =
                user.getBalance()
                        .add(
                                request.getAmount()
                        );

        user.setBalance(
                updatedBalance
        );

        paymentService
                .createWalletCreditEntry(
                        user,
                        request.getAmount(),
                        request.getPaymentMethod(),
                        PaymentConstants.SUCCESS
                );

        return userRepository
                .save(user);
    }
}