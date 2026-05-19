package com.personal.project.service.impl;

import com.personal.project.service.WalletService;
import com.personal.project.service.PaymentService;

import com.personal.project.constants.PaymentConstants;
import com.personal.project.dto.WalletTopupRequest;
import com.personal.project.dto.UserDTO;
import com.personal.project.entity.User;
import com.personal.project.exception.InvalidQuantityException;
import com.personal.project.exception.UserNotFoundException;
import com.personal.project.mapper.UserMapper;
import com.personal.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.personal.project.config.RedisCacheConfig.USER_DASHBOARD_CACHE;
import static com.personal.project.config.RedisCacheConfig.USER_PAYMENTS_CACHE;

@Service
public class WalletServiceImpl
        implements WalletService {

    private final UserRepository
            userRepository;

    private final PaymentService
            paymentService;

    private final UserMapper
            userMapper;

    public WalletServiceImpl(
            UserRepository userRepository,
            PaymentService paymentService,
            UserMapper userMapper
    ) {

        this.userRepository =
                userRepository;

        this.paymentService =
                paymentService;

        this.userMapper =
                userMapper;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {
            USER_DASHBOARD_CACHE,
            USER_PAYMENTS_CACHE
    }, key = "#request.userId")
    public UserDTO topupWallet(
            WalletTopupRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }

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
                        .findByUserIdForUpdate(
                                request.getUserId()
                        )
                        .orElseThrow(
                                () ->
                                        new UserNotFoundException(
                                                "User not found"
                                        )
                        );

        BigDecimal currentBalance =
                user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO;

        BigDecimal updatedBalance =
                currentBalance.add(
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

        User savedUser = userRepository
                .save(user);

        return userMapper.toDto(savedUser);
    }
}
