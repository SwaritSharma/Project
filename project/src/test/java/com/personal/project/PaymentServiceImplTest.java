package com.personal.project;

import com.personal.project.dtos.AddMoneyToWalletRequestDto;
import com.personal.project.dtos.PaymentResponseDto;
import com.personal.project.entity.Payment;
import com.personal.project.entity.User;
import com.personal.project.enums.PaymentMethod;
import com.personal.project.enums.PaymentStatus;
import com.personal.project.enums.PaymentTransactionType;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.PaymentMapper;
import com.personal.project.repositories.PaymentRepository;
import com.personal.project.repositories.TransactionHistoryRepository;
import com.personal.project.repositories.UserRepository;
import com.personal.project.repositories.VendorRepository;
import com.personal.project.servicesImpl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private TransactionHistoryRepository
            transactionHistoryRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private User user;

    private Payment payment;

    private PaymentResponseDto paymentResponseDto;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .userId(1)
                .balance(new BigDecimal("1000"))
                .build();

        payment = Payment.builder()
                .paymentId(1)
                .user(user)
                .amount(new BigDecimal("500"))
                .paymentMethod(
                        PaymentMethod.GOOGLE_PAY
                )
                .paymentStatus(
                        PaymentStatus.SUCCESS
                )
                .transactionType(
                        PaymentTransactionType
                                .CREDITED_TO_WALLET
                )
                .build();

        paymentResponseDto =
                PaymentResponseDto.builder()
                        .paymentId(1)
                        .amount("500")
                        .paymentMethod("GOOGLE_PAY")
                        .paymentStatus("SUCCESS")
                        .transactionType(
                                "CREDITED_TO_WALLET"
                        )
                        .build();
    }

    @Test
    void addMoneyToWallet_ShouldAddMoneySuccessfully() {

        AddMoneyToWalletRequestDto dto =
                AddMoneyToWalletRequestDto.builder()
                        .amount(new BigDecimal("500"))
                        .paymentMethod(
                                PaymentMethod.GOOGLE_PAY
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);

        when(paymentMapper
                .toPaymentResponseDto(payment))
                .thenReturn(paymentResponseDto);

        PaymentResponseDto result =
                paymentService
                        .addMoneyToWallet(1, dto);

        assertNotNull(result);

        assertEquals(
                "1500",
                user.getBalance()
                        .toPlainString()
        );

        assertEquals(
                "500",
                result.getAmount()
        );

        verify(userRepository, times(1))
                .save(user);

        verify(paymentRepository, times(1))
                .save(any(Payment.class));
    }

    @Test
    void addMoneyToWallet_ShouldThrowException_WhenUserNotFound() {

        AddMoneyToWalletRequestDto dto =
                AddMoneyToWalletRequestDto.builder()
                        .amount(new BigDecimal("500"))
                        .paymentMethod(
                                PaymentMethod.GOOGLE_PAY
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> paymentService
                        .addMoneyToWallet(1, dto)
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void getUserPayments_ShouldReturnPaymentsSuccessfully() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Payment> paymentPage =
                new PageImpl<>(
                        Collections.singletonList(
                                payment
                        )
                );

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(paymentRepository
                .findByUserUserId(
                        1,
                        pageable
                ))
                .thenReturn(paymentPage);

        when(paymentMapper
                .toPaymentResponseDto(payment))
                .thenReturn(paymentResponseDto);

        Page<PaymentResponseDto> result =
                paymentService
                        .getUserPayments(
                                1,
                                pageable
                        );

        assertEquals(
                1,
                result.getTotalElements()
        );

        verify(paymentRepository, times(1))
                .findByUserUserId(
                        1,
                        pageable
                );
    }

    @Test
    void getUserPayments_ShouldThrowException_WhenUserNotFound() {

        Pageable pageable =
                PageRequest.of(0, 10);

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> paymentService
                        .getUserPayments(
                                1,
                                pageable
                        )
        );
    }

    @Test
    void getUserPaymentsByMethod_ShouldReturnPaymentsSuccessfully() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Payment> paymentPage =
                new PageImpl<>(
                        Collections.singletonList(
                                payment
                        )
                );

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(paymentRepository
                .findByUserUserIdAndPaymentMethod(
                        1,
                        PaymentMethod.GOOGLE_PAY,
                        pageable
                ))
                .thenReturn(paymentPage);

        when(paymentMapper
                .toPaymentResponseDto(payment))
                .thenReturn(paymentResponseDto);

        Page<PaymentResponseDto> result =
                paymentService
                        .getUserPaymentsByMethod(
                                1,
                                PaymentMethod.GOOGLE_PAY,
                                pageable
                        );

        assertEquals(
                1,
                result.getTotalElements()
        );
    }

    @Test
    void getUserPaymentsByStatus_ShouldReturnPaymentsSuccessfully() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Payment> paymentPage =
                new PageImpl<>(
                        Collections.singletonList(
                                payment
                        )
                );

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(paymentRepository
                .findByUserUserIdAndPaymentStatus(
                        1,
                        PaymentStatus.SUCCESS,
                        pageable
                ))
                .thenReturn(paymentPage);

        when(paymentMapper
                .toPaymentResponseDto(payment))
                .thenReturn(paymentResponseDto);

        Page<PaymentResponseDto> result =
                paymentService
                        .getUserPaymentsByStatus(
                                1,
                                PaymentStatus.SUCCESS,
                                pageable
                        );

        assertEquals(
                1,
                result.getTotalElements()
        );
    }

    @Test
    void getUserPaymentsSortedByAmountDesc_ShouldReturnPaymentsSuccessfully() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Payment> paymentPage =
                new PageImpl<>(
                        Collections.singletonList(
                                payment
                        )
                );

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(paymentRepository
                .findByUserUserIdOrderByAmountDesc(
                        1,
                        pageable
                ))
                .thenReturn(paymentPage);

        when(paymentMapper
                .toPaymentResponseDto(payment))
                .thenReturn(paymentResponseDto);

        Page<PaymentResponseDto> result =
                paymentService
                        .getUserPaymentsSortedByAmountDesc(
                                1,
                                pageable
                        );

        assertEquals(
                1,
                result.getTotalElements()
        );
    }
}