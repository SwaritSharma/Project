package com.personal.project.servicesImpl;

import com.personal.project.dtos.BuyVirtualGoldRequestDto;
import com.personal.project.dtos.SellVirtualGoldRequestDto;
import com.personal.project.dtos.VirtualGoldHoldingResponseDto;
import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.User;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import com.personal.project.entity.VirtualGoldHolding;
import com.personal.project.exceptions.InsufficientBalanceException;
import com.personal.project.exceptions.InsufficientGoldQuantityException;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.VirtualGoldHoldingMapper;
import com.personal.project.repositories.TransactionHistoryRepository;
import com.personal.project.repositories.UserRepository;
import com.personal.project.repositories.VendorBranchRepository;
import com.personal.project.repositories.VendorRepository;
import com.personal.project.repositories.VirtualGoldHoldingRepository;
import com.personal.project.services.VirtualGoldHoldingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VirtualGoldHoldingServiceImpl
        implements VirtualGoldHoldingService {

    private final UserRepository userRepository;

    private final VendorRepository vendorRepository;

    private final VendorBranchRepository
            vendorBranchRepository;

    private final VirtualGoldHoldingRepository
            virtualGoldHoldingRepository;

    private final TransactionHistoryRepository
            transactionHistoryRepository;

    private final VirtualGoldHoldingMapper
            virtualGoldHoldingMapper;

    @Override
    public VirtualGoldHoldingResponseDto buyVirtualGold(
            Integer userId,
            BuyVirtualGoldRequestDto dto
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

        user.setBalance(
                user.getBalance()
                        .subtract(amount)
        );

        userRepository.save(user);

        VirtualGoldHolding holding =
                virtualGoldHoldingRepository
                        .findByUserUserIdAndBranchBranchId(
                                userId,
                                branch.getBranchId()
                        )
                        .orElse(
                                VirtualGoldHolding.builder()
                                        .user(user)
                                        .branch(branch)
                                        .quantity(BigDecimal.ZERO)
                                        .build()
                        );

        holding.setQuantity(
                holding.getQuantity()
                        .add(dto.getQuantity())
        );

        VirtualGoldHolding savedHolding =
                virtualGoldHoldingRepository.save(
                        holding
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

        return virtualGoldHoldingMapper
                .toVirtualGoldHoldingResponseDto(
                        savedHolding
                );
    }

    @Override
    public VirtualGoldHoldingResponseDto sellVirtualGold(
            Integer userId,
            SellVirtualGoldRequestDto dto
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

        BigDecimal amount =
                vendor.getCurrentGoldPrice()
                        .multiply(dto.getQuantity());

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

        branch.setQuantity(
                branch.getQuantity()
                        .add(dto.getQuantity())
        );

        vendorBranchRepository.save(branch);

        vendor.setTotalGoldQuantity(
                vendor.getTotalGoldQuantity()
                        .add(dto.getQuantity())
        );

        vendorRepository.save(vendor);

        user.setBalance(
                user.getBalance()
                        .add(amount)
        );

        userRepository.save(user);

        TransactionHistory transactionHistory =
                TransactionHistory.builder()
                        .user(user)
                        .branch(branch)
                        .transactionType(
                                com.personal.project.enums
                                        .TransactionType
                                        .SELL
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

        return virtualGoldHoldingMapper
                .toVirtualGoldHoldingResponseDto(
                        holding
                );
    }

    @Override
    public Page<VirtualGoldHoldingResponseDto>
    getUserVirtualGoldHoldings(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return virtualGoldHoldingRepository
                .findByUserUserId(
                        userId,
                        pageable
                )
                .map(
                        virtualGoldHoldingMapper
                                ::toVirtualGoldHoldingResponseDto
                );
    }

    @Override
    public Page<VirtualGoldHoldingResponseDto>
    getUserVirtualGoldHoldingsSortedByQuantityDesc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return virtualGoldHoldingRepository
                .findByUserUserIdOrderByQuantityDesc(
                        userId,
                        pageable
                )
                .map(
                        virtualGoldHoldingMapper
                                ::toVirtualGoldHoldingResponseDto
                );
    }

    @Override
    public Page<VirtualGoldHoldingResponseDto>
    getUserVirtualGoldHoldingsSortedByQuantityAsc(
            Integer userId,
            Pageable pageable
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return virtualGoldHoldingRepository
                .findByUserUserIdOrderByQuantityAsc(
                        userId,
                        pageable
                )
                .map(
                        virtualGoldHoldingMapper
                                ::toVirtualGoldHoldingResponseDto
                );
    }
}