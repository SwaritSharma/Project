package com.personal.project.servicesImpl;

import com.personal.project.dtos.AddMoneyToWalletRequestDto;
import com.personal.project.dtos.PaymentResponseDto;
import com.personal.project.dtos.TransactionHistoryResponseDto;
import com.personal.project.entity.Payment;
import com.personal.project.entity.User;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.PaymentMapper;
import com.personal.project.repositories.PaymentRepository;
import com.personal.project.repositories.TransactionHistoryRepository;
import com.personal.project.repositories.UserRepository;
import com.personal.project.repositories.VendorRepository;
import com.personal.project.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {

    private final UserRepository userRepository;

    private final VendorRepository vendorRepository;

    private final PaymentRepository paymentRepository;

    private final TransactionHistoryRepository
            transactionHistoryRepository;

    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDto addMoneyToWallet(
            Integer userId,
            AddMoneyToWalletRequestDto dto
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        user.setBalance(
                user.getBalance()
                        .add(dto.getAmount())
        );

        userRepository.save(user);

        Payment payment =
                Payment.builder()
                        .user(user)
                        .amount(dto.getAmount())
                        .paymentMethod(dto.getPaymentMethod())
                        .paymentStatus(
                                com.personal.project.enums
                                        .PaymentStatus.SUCCESS
                        )
                        .transactionType(
                                com.personal.project.enums
                                        .PaymentTransactionType
                                        .CREDITED_TO_WALLET
                        )
                        .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        return paymentMapper.toPaymentResponseDto(
                savedPayment
        );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getUserTransactions(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return transactionHistoryRepository
                .findByUserUserId(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getUserTransactionsByType(
            Integer userId,
            com.personal.project.enums.TransactionType
                    transactionType,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return transactionHistoryRepository
                .findByUserUserIdAndTransactionType(
                        userId,
                        transactionType,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getUserTransactionsByStatus(
            Integer userId,
            com.personal.project.enums.TransactionStatus
                    transactionStatus,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return transactionHistoryRepository
                .findByUserUserIdAndTransactionStatus(
                        userId,
                        transactionStatus,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getUserTransactionsSortedByCreatedAtDesc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return transactionHistoryRepository
                .findByUserUserIdOrderByCreatedAtDesc(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getUserTransactionsSortedByCreatedAtAsc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return transactionHistoryRepository
                .findByUserUserIdOrderByCreatedAtAsc(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getUserTransactionsSortedByAmountDesc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return transactionHistoryRepository
                .findByUserUserIdOrderByAmountDesc(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getUserTransactionsSortedByAmountAsc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return transactionHistoryRepository
                .findByUserUserIdOrderByAmountAsc(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<PaymentResponseDto>
    getUserPayments(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return paymentRepository
                .findByUserUserId(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toPaymentResponseDto
                );
    }

    @Override
    public Page<PaymentResponseDto>
    getUserPaymentsByMethod(
            Integer userId,
            com.personal.project.enums.PaymentMethod
                    paymentMethod,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return paymentRepository
                .findByUserUserIdAndPaymentMethod(
                        userId,
                        paymentMethod,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toPaymentResponseDto
                );
    }

    @Override
    public Page<PaymentResponseDto>
    getUserPaymentsByTransactionType(
            Integer userId,
            com.personal.project.enums.PaymentTransactionType
                    transactionType,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return paymentRepository
                .findByUserUserIdAndTransactionType(
                        userId,
                        transactionType,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toPaymentResponseDto
                );
    }

    @Override
    public Page<PaymentResponseDto>
    getUserPaymentsByStatus(
            Integer userId,
            com.personal.project.enums.PaymentStatus
                    paymentStatus,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return paymentRepository
                .findByUserUserIdAndPaymentStatus(
                        userId,
                        paymentStatus,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toPaymentResponseDto
                );
    }

    @Override
    public Page<PaymentResponseDto>
    getUserPaymentsSortedByCreatedAtDesc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return paymentRepository
                .findByUserUserIdOrderByCreatedAtDesc(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toPaymentResponseDto
                );
    }

    @Override
    public Page<PaymentResponseDto>
    getUserPaymentsSortedByCreatedAtAsc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return paymentRepository
                .findByUserUserIdOrderByCreatedAtAsc(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toPaymentResponseDto
                );
    }

    @Override
    public Page<PaymentResponseDto>
    getUserPaymentsSortedByAmountDesc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return paymentRepository
                .findByUserUserIdOrderByAmountDesc(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toPaymentResponseDto
                );
    }

    @Override
    public Page<PaymentResponseDto>
    getUserPaymentsSortedByAmountAsc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return paymentRepository
                .findByUserUserIdOrderByAmountAsc(
                        userId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toPaymentResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getVendorTransactions(
            Integer vendorId,
            Pageable pageable
    ) {

        vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor not found"
                        )
                );

        return transactionHistoryRepository
                .findByBranchVendorVendorId(
                        vendorId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getVendorTransactionsByType(
            Integer vendorId,
            com.personal.project.enums.TransactionType
                    transactionType,
            Pageable pageable
    ) {

        vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor not found"
                        )
                );

        return transactionHistoryRepository
                .findByBranchVendorVendorIdAndTransactionType(
                        vendorId,
                        transactionType,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getVendorTransactionsByStatus(
            Integer vendorId,
            com.personal.project.enums.TransactionStatus
                    transactionStatus,
            Pageable pageable
    ) {

        vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor not found"
                        )
                );

        return transactionHistoryRepository
                .findByBranchVendorVendorIdAndTransactionStatus(
                        vendorId,
                        transactionStatus,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getVendorTransactionsByBranch(
            Integer branchId,
            Pageable pageable
    ) {

        return transactionHistoryRepository
                .findByBranchBranchId(
                        branchId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getVendorTransactionsSortedByCreatedAtDesc(
            Integer vendorId,
            Pageable pageable
    ) {

        vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor not found"
                        )
                );

        return transactionHistoryRepository
                .findByBranchVendorVendorIdOrderByCreatedAtDesc(
                        vendorId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getVendorTransactionsSortedByCreatedAtAsc(
            Integer vendorId,
            Pageable pageable
    ) {

        vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor not found"
                        )
                );

        return transactionHistoryRepository
                .findByBranchVendorVendorIdOrderByCreatedAtAsc(
                        vendorId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getVendorTransactionsSortedByAmountDesc(
            Integer vendorId,
            Pageable pageable
    ) {

        vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor not found"
                        )
                );

        return transactionHistoryRepository
                .findByBranchVendorVendorIdOrderByAmountDesc(
                        vendorId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }

    @Override
    public Page<TransactionHistoryResponseDto>
    getVendorTransactionsSortedByAmountAsc(
            Integer vendorId,
            Pageable pageable
    ) {

        vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor not found"
                        )
                );

        return transactionHistoryRepository
                .findByBranchVendorVendorIdOrderByAmountAsc(
                        vendorId,
                        pageable
                )
                .map(
                        paymentMapper
                                ::toTransactionHistoryResponseDto
                );
    }
}