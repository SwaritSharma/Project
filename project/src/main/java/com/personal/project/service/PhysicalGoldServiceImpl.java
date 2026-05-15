package com.personal.project.service;

import com.personal.project.constants.PaymentConstants;
import com.personal.project.constants.TransactionConstants;
import com.personal.project.dto.BuyPhysicalGoldRequest;
import com.personal.project.dto.ConvertToPhysicalGoldRequest;
import com.personal.project.entity.Address;
import com.personal.project.entity.PhysicalGoldTransaction;
import com.personal.project.entity.User;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import com.personal.project.entity.VirtualGoldHolding;
import com.personal.project.exception.*;
import com.personal.project.repository.AddressRepository;
import com.personal.project.repository.PhysicalGoldTransactionRepository;
import com.personal.project.repository.UserRepository;
import com.personal.project.repository.VendorBranchRepository;
import com.personal.project.repository.VendorRepository;
import com.personal.project.repository.VirtualGoldHoldingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PhysicalGoldServiceImpl
        implements PhysicalGoldService {

    private final UserRepository
            userRepository;

    private final VendorRepository
            vendorRepository;

    private final VendorBranchRepository
            vendorBranchRepository;

    private final AddressRepository
            addressRepository;

    private final
    VirtualGoldHoldingRepository
            holdingRepository;

    private final
    PhysicalGoldTransactionRepository
            physicalGoldTransactionRepository;

    private final
    BranchAllocationService
            branchAllocationService;

    private final PaymentService
            paymentService;

    private final
    TransactionHistoryService
            transactionHistoryService;

    public PhysicalGoldServiceImpl(
            UserRepository userRepository,
            VendorRepository vendorRepository,
            VendorBranchRepository
                    vendorBranchRepository,
            AddressRepository
                    addressRepository,
            VirtualGoldHoldingRepository
                    holdingRepository,
            PhysicalGoldTransactionRepository
                    physicalGoldTransactionRepository,
            BranchAllocationService
                    branchAllocationService,
            PaymentService paymentService,
            TransactionHistoryService
                    transactionHistoryService
    ) {

        this.userRepository =
                userRepository;

        this.vendorRepository =
                vendorRepository;

        this.vendorBranchRepository =
                vendorBranchRepository;

        this.addressRepository =
                addressRepository;

        this.holdingRepository =
                holdingRepository;

        this.physicalGoldTransactionRepository =
                physicalGoldTransactionRepository;

        this.branchAllocationService =
                branchAllocationService;

        this.paymentService =
                paymentService;

        this.transactionHistoryService =
                transactionHistoryService;
    }

    @Override
    @Transactional
    public PhysicalGoldTransaction
    buyPhysicalGold(
            BuyPhysicalGoldRequest request
    ) {

        if (
                request.getQuantity()
                        .compareTo(BigDecimal.ZERO)
                        <= 0
        ) {

            throw new InvalidQuantityException(
                    "Quantity must be greater than 0"
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

        Vendor vendor =
                vendorRepository
                        .findById(
                                request.getVendorId()
                        )
                        .orElseThrow(
                                () ->
                                        new VendorNotFoundException(
                                                "Vendor not found"
                                        )
                        );

        Address deliveryAddress =
                addressRepository
                        .findById(
                                request.getDeliveryAddressId()
                        )
                        .orElseThrow(
                                () ->
                                        new AddressNotFoundException(
                                                "Delivery address not found"
                                        )
                        );

        VendorBranch allocatedBranch =
                branchAllocationService
                        .allocateBranch(
                                vendor.getVendorId(),
                                deliveryAddress
                                        .getAddressId(),
                                request.getQuantity()
                        );

        BigDecimal totalAmount =
                vendor.getCurrentGoldPrice()
                        .multiply(
                                request.getQuantity()
                        );

        if (
                user.getBalance()
                        .compareTo(totalAmount)
                        < 0
        ) {

            throw new InsufficientWalletBalanceException(
                    "Insufficient wallet balance"
            );
        }

        user.setBalance(
                user.getBalance()
                        .subtract(totalAmount)
        );

        allocatedBranch.setQuantity(
                allocatedBranch.getQuantity()
                        .subtract(
                                request.getQuantity()
                        )
        );

        PhysicalGoldTransaction
                transaction =
                new PhysicalGoldTransaction();

        transaction.setUser(user);

        transaction.setBranch(
                allocatedBranch
        );

        transaction.setQuantity(
                request.getQuantity()
        );

        transaction.setDeliveryAddress(
                deliveryAddress
        );

        transaction.setCreatedAt(
                LocalDateTime.now()
        );

        paymentService
                .createWalletDebitEntry(
                        user,
                        totalAmount,
                        PaymentConstants
                                .BANK_TRANSFER,
                        PaymentConstants.SUCCESS
                );

        transactionHistoryService
                .createBuyTransaction(
                        user,
                        allocatedBranch,
                        request.getQuantity(),
                        totalAmount,
                        TransactionConstants.SUCCESS
                );

        userRepository.save(user);

        vendorBranchRepository
                .save(allocatedBranch);

        return physicalGoldTransactionRepository
                .save(transaction);
    }

    @Override
    @Transactional
    public PhysicalGoldTransaction
    convertToPhysicalGold(
            ConvertToPhysicalGoldRequest request
    ) {

        if (
                request.getQuantity()
                        .compareTo(BigDecimal.ZERO)
                        <= 0
        ) {

            throw new InvalidQuantityException(
                    "Quantity must be greater than 0"
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

        if (user.getAddress() == null) {

            throw new AddressNotFoundException(
                    "User address not found"
            );
        }

        VirtualGoldHolding holding =
                holdingRepository
                        .findById(
                                request.getHoldingId()
                        )
                        .orElseThrow(
                                () ->
                                        new HoldingNotFoundException(
                                                "Holding not found"
                                        )
                        );

        if (
                !holding.getUser()
                        .getUserId()
                        .equals(
                                request.getUserId()
                        )
        ) {

            throw new UnauthorizedHoldingAccessException(
                    "Holding does not belong to user"
            );
        }

        Address deliveryAddress =
                addressRepository
                        .findById(
                                request.getDeliveryAddressId()
                        )
                        .orElseThrow(
                                () ->
                                        new AddressNotFoundException(
                                                "Delivery address not found"
                                        )
                        );

        if (
                holding.getQuantity()
                        .compareTo(
                                request.getQuantity()
                        )
                        < 0
        ) {

            throw new InsufficientHoldingQuantityException(
                    "Insufficient holding quantity"
            );
        }

        VendorBranch allocatedBranch =
                branchAllocationService
                        .allocateBranch(
                                holding.getBranch()
                                        .getVendor()
                                        .getVendorId(),
                                deliveryAddress
                                        .getAddressId(),
                                request.getQuantity()
                        );

        allocatedBranch.setQuantity(
                allocatedBranch.getQuantity()
                        .subtract(
                                request.getQuantity()
                        )
        );

        holding.setQuantity(
                holding.getQuantity()
                        .subtract(
                                request.getQuantity()
                        )
        );

        BigDecimal totalAmount =
                allocatedBranch
                        .getVendor()
                        .getCurrentGoldPrice()
                        .multiply(
                                request.getQuantity()
                        );

        PhysicalGoldTransaction
                transaction =
                new PhysicalGoldTransaction();

        transaction.setUser(user);

        transaction.setBranch(
                allocatedBranch
        );

        transaction.setQuantity(
                request.getQuantity()
        );

        transaction.setDeliveryAddress(
                deliveryAddress
        );

        transaction.setCreatedAt(
                LocalDateTime.now()
        );

        transactionHistoryService
                .createConvertToPhysicalTransaction(
                        user,
                        allocatedBranch,
                        request.getQuantity(),
                        totalAmount,
                        TransactionConstants.SUCCESS
                );

        vendorBranchRepository
                .save(allocatedBranch);

        if (
                holding.getQuantity()
                        .compareTo(BigDecimal.ZERO)
                        == 0
        ) {

            holdingRepository.delete(
                    holding
            );
        }

        else {

            holdingRepository.save(
                    holding
            );
        }

        return physicalGoldTransactionRepository
                .save(transaction);
    }
}