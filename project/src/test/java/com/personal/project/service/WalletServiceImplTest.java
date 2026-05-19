package com.personal.project.service;

import com.personal.project.service.impl.WalletServiceImpl;
import com.personal.project.constants.PaymentConstants;
import com.personal.project.dto.WalletTopupRequest;
import com.personal.project.dto.UserDTO;
import com.personal.project.entity.User;
import com.personal.project.mapper.UserMapper;
import com.personal.project.exception.InvalidQuantityException;
import com.personal.project.exception.UserNotFoundException;
import com.personal.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private UserRepository
            userRepository;

    @Mock
    private PaymentService
            paymentService;

    @Mock
    private UserMapper
            userMapper;

    @InjectMocks
    private WalletServiceImpl
            walletService;

    private User user;

    private WalletTopupRequest request;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setUserId(1);

        user.setBalance(
                new BigDecimal("1000")
        );

        request =
                new WalletTopupRequest();

        request.setUserId(1);

        request.setAmount(
                new BigDecimal("500")
        );

        request.setPaymentMethod(
                PaymentConstants
                        .BANK_TRANSFER
        );
    }

    @Test
    void topupWallet_ShouldAddBalanceSuccessfully() {

        when(
                userRepository.findByUserIdForUpdate(1)
        ).thenReturn(
                Optional.of(user)
        );

        when(
                userRepository.save(user)
        ).thenReturn(user);

        when(
                userMapper.toDto(any(User.class))
        ).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            UserDTO dto = new UserDTO();
            dto.setUserId(u.getUserId());
            dto.setBalance(u.getBalance());
            return dto;
        });

        UserDTO savedUser =
                walletService
                        .topupWallet(request);

        assertEquals(
                new BigDecimal("1500"),
                savedUser.getBalance()
        );

        verify(userRepository)
                .save(user);

        verify(paymentService)
                .createWalletCreditEntry(
                        eq(user),
                        eq(new BigDecimal("500")),
                        eq(PaymentConstants.BANK_TRANSFER),
                        eq(PaymentConstants.SUCCESS)
                );
    }

    @Test
    void topupWallet_ShouldThrowInvalidQuantityException_WhenAmountIsZero() {

        request.setAmount(
                BigDecimal.ZERO
        );

        assertThrows(
                InvalidQuantityException.class,
                () ->
                        walletService
                                .topupWallet(request)
        );

        verify(userRepository,
                never()
        ).findByUserIdForUpdate(anyInt());
    }

    @Test
    void topupWallet_ShouldThrowUserNotFoundException() {

        when(
                userRepository.findByUserIdForUpdate(1)
        ).thenReturn(
                Optional.empty()
        );

        assertThrows(
                UserNotFoundException.class,
                () ->
                        walletService
                                .topupWallet(request)
        );

        verify(userRepository,
                never()
        ).save(any(User.class));
    }

    @Test
    void topupWallet_ShouldThrowInvalidQuantityException_WhenAmountIsNegative() {

        request.setAmount(
                new BigDecimal("-100")
        );

        assertThrows(
                InvalidQuantityException.class,
                () ->
                        walletService
                                .topupWallet(request)
        );

        verify(userRepository,
                never()
        ).findByUserIdForUpdate(anyInt());
    }

    @Test
    void topupWallet_ShouldCallPaymentService() {

        when(
                userRepository.findByUserIdForUpdate(1)
        ).thenReturn(
                Optional.of(user)
        );

        when(
                userRepository.save(user)
        ).thenReturn(user);

        when(
                userMapper.toDto(any(User.class))
        ).thenReturn(new UserDTO());

        walletService
                .topupWallet(request);

        verify(paymentService,
                times(1)
        ).createWalletCreditEntry(
                eq(user),
                eq(new BigDecimal("500")),
                eq(PaymentConstants.BANK_TRANSFER),
                eq(PaymentConstants.SUCCESS)
        );
    }
}