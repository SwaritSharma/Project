package com.personal.project.servicesImpl;

import com.personal.project.dtos.BuyPhysicalGoldRequestDto;
import com.personal.project.dtos.ConvertVirtualToPhysicalRequestDto;
import com.personal.project.dtos.ConvertVirtualToPhysicalResponseDto;
import com.personal.project.dtos.PhysicalGoldTransactionResponseDto;
import com.personal.project.entity.PhysicalGoldTransaction;
import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.User;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import com.personal.project.entity.VirtualGoldHolding;
import com.personal.project.exceptions.InsufficientBalanceException;
import com.personal.project.exceptions.InsufficientGoldQuantityException;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.PhysicalGoldTransactionMapper;
import com.personal.project.repositories.PhysicalGoldTransactionRepository;
import com.personal.project.repositories.TransactionHistoryRepository;
import com.personal.project.repositories.UserRepository;
import com.personal.project.repositories.VendorBranchRepository;
import com.personal.project.repositories.VendorRepository;
import com.personal.project.repositories.VirtualGoldHoldingRepository;
import com.personal.project.services.PhysicalGoldTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PhysicalGoldTransactionServiceImpl
        implements PhysicalGoldTransactionService {

    private final UserRepository userRepository;

    private final VendorRepository vendorRepository;

    private final VendorBranchRepository
            vendorBranchRepository;

    private final VirtualGoldHoldingRepository
            virtualGoldHoldingRepository;

    private final PhysicalGoldTransactionRepository
            physicalGoldTransactionRepository;

    private final TransactionHistoryRepository
            transactionHistoryRepository;

    private final PhysicalGoldTransactionMapper
            physicalGoldTransactionMapper;

    @Override
    public ConvertVirtualToPhysicalResponseDto
    convertVirtualToPhysical(
            Integer userId,
            ConvertVirtualToPhysicalRequestDto dto
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        Vendor vendor =
                vendorRepository.findById(dto.getVendorId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vendor not found"
                                )
                        );

        String postalCode =
                user.getAddress()
                        .getPostalCode();

        VendorBranch branch =
                vendorBranchRepository
                        .findFirstByVendorVendorIdAndAddressPostalCodeOrderByQuantityDesc(
                                dto.getVendorId(),
                                postalCode
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Suitable branch not found"
                                )
                        );

        VirtualGoldHolding holding =
                virtualGoldHoldingRepository
                        .findByUserUserIdAndBranchBranchId(
                                userId,
                                branch.getBranchId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Virtual gold holding not found"
                                )
                        );

        if (
                holding.getQuantity()
                        .compareTo(dto.getQuantity()) < 0
        ) {

            throw new InsufficientGoldQuantityException(
                    "Insufficient virtual gold quantity"
            );
        }

        holding.setQuantity(
                holding.getQuantity()
                        .subtract(dto.getQuantity())
        );

        if (
                holding.getQuantity()
                        .compareTo(BigDecimal.ZERO) == 0
        ) {

            virtualGoldHoldingRepository.delete(
                    holding
            );

        } else {

            virtualGoldHoldingRepository.save(
                    holding
            );
        }

        PhysicalGoldTransaction
                physicalGoldTransaction =
                PhysicalGoldTransaction.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(dto.getQuantity())
                        .deliveryAddress(
                                user.getAddress()
                        )
                        .build();

        PhysicalGoldTransaction savedTransaction =
                physicalGoldTransactionRepository.save(
                        physicalGoldTransaction
                );

        BigDecimal amount =
                vendor.getCurrentGoldPrice()
                        .multiply(dto.getQuantity());

        TransactionHistory transactionHistory =
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(
                                com.personal.project.enums
                                        .TransactionType
                                        .CONVERT_TO_PHYSICAL
                        )
                        .transactionStatus(
                                com.personal.project.enums
                                        .TransactionStatus
                                        .SUCCESS
                        )
                        .quantity(dto.getQuantity())
                        .amount(amount)
                        .build();

        transactionHistoryRepository.save(
                transactionHistory
        );

        return physicalGoldTransactionMapper
                .toConvertVirtualToPhysicalResponseDto(
                        savedTransaction
                );
    }

    @Override
    public PhysicalGoldTransactionResponseDto
    buyPhysicalGold(
            Integer userId,
            BuyPhysicalGoldRequestDto dto
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        Vendor vendor =
                vendorRepository.findById(dto.getVendorId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vendor not found"
                                )
                        );

        String postalCode =
                user.getAddress()
                        .getPostalCode();

        VendorBranch branch =
                vendorBranchRepository
                        .findFirstByVendorVendorIdAndAddressPostalCodeAndQuantityGreaterThanEqualOrderByQuantityDesc(
                                dto.getVendorId(),
                                postalCode,
                                dto.getQuantity()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Suitable branch not found"
                                )
                        );

        BigDecimal amount =
                vendor.getCurrentGoldPrice()
                        .multiply(dto.getQuantity());

        if (
                user.getBalance().compareTo(amount) < 0
        ) {

            throw new InsufficientBalanceException(
                    "Insufficient wallet balance"
            );
        }

        user.setBalance(
                user.getBalance()
                        .subtract(amount)
        );

        userRepository.save(user);

        branch.setQuantity(
                branch.getQuantity()
                        .subtract(dto.getQuantity())
        );

        vendorBranchRepository.save(branch);

        vendor.setTotalGoldQuantity(
                vendor.getTotalGoldQuantity()
                        .subtract(dto.getQuantity())
        );

        vendorRepository.save(vendor);

        PhysicalGoldTransaction
                physicalGoldTransaction =
                PhysicalGoldTransaction.builder()
                        .user(user)
                        .branch(branch)
                        .quantity(dto.getQuantity())
                        .deliveryAddress(
                                user.getAddress()
                        )
                        .build();

        PhysicalGoldTransaction savedTransaction =
                physicalGoldTransactionRepository.save(
                        physicalGoldTransaction
                );

        TransactionHistory transactionHistory =
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(
                                com.personal.project.enums
                                        .TransactionType
                                        .BUY
                        )
                        .transactionStatus(
                                com.personal.project.enums
                                        .TransactionStatus
                                        .SUCCESS
                        )
                        .quantity(dto.getQuantity())
                        .amount(amount)
                        .build();

        transactionHistoryRepository.save(
                transactionHistory
        );

        return physicalGoldTransactionMapper
                .toPhysicalGoldTransactionResponseDto(
                        savedTransaction
                );
    }

    @Override
    public Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactions(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return physicalGoldTransactionRepository
                .findByUserUserId(
                        userId,
                        pageable
                )
                .map(
                        physicalGoldTransactionMapper
                                ::toPhysicalGoldTransactionResponseDto
                );
    }

    @Override
    public Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsByBranch(
            Integer userId,
            Integer branchId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return physicalGoldTransactionRepository
                .findByUserUserIdAndBranchBranchId(
                        userId,
                        branchId,
                        pageable
                )
                .map(
                        physicalGoldTransactionMapper
                                ::toPhysicalGoldTransactionResponseDto
                );
    }

    @Override
    public Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsSortedByCreatedAtDesc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return physicalGoldTransactionRepository
                .findByUserUserIdOrderByCreatedAtDesc(
                        userId,
                        pageable
                )
                .map(
                        physicalGoldTransactionMapper
                                ::toPhysicalGoldTransactionResponseDto
                );
    }

    @Override
    public Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsSortedByCreatedAtAsc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return physicalGoldTransactionRepository
                .findByUserUserIdOrderByCreatedAtAsc(
                        userId,
                        pageable
                )
                .map(
                        physicalGoldTransactionMapper
                                ::toPhysicalGoldTransactionResponseDto
                );
    }

    @Override
    public Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsSortedByQuantityDesc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return physicalGoldTransactionRepository
                .findByUserUserIdOrderByQuantityDesc(
                        userId,
                        pageable
                )
                .map(
                        physicalGoldTransactionMapper
                                ::toPhysicalGoldTransactionResponseDto
                );
    }

    @Override
    public Page<PhysicalGoldTransactionResponseDto>
    getUserPhysicalGoldTransactionsSortedByQuantityAsc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return physicalGoldTransactionRepository
                .findByUserUserIdOrderByQuantityAsc(
                        userId,
                        pageable
                )
                .map(
                        physicalGoldTransactionMapper
                                ::toPhysicalGoldTransactionResponseDto
                );
    }
}