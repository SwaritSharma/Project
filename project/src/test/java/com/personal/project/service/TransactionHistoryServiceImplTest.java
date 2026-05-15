package com.personal.project.service;

import com.personal.project.constants.TransactionConstants;
import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.User;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import com.personal.project.repository.TransactionHistoryRepository;
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
class TransactionHistoryServiceImplTest {

    @Mock
    private TransactionHistoryRepository
            transactionHistoryRepository;

    @InjectMocks
    private TransactionHistoryServiceImpl
            transactionHistoryService;

    private User user;

    private Vendor vendor;

    private VendorBranch branch;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setUserId(1);

        vendor = new Vendor();

        vendor.setVendorId(1);

        branch = new VendorBranch();

        branch.setBranchId(1);

        branch.setVendor(vendor);
    }

    @Test
    void createBuyTransaction_ShouldCreateSuccessfully() {

        when(
                transactionHistoryRepository.save(
                        any(TransactionHistory.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );

        TransactionHistory transaction =
                transactionHistoryService
                        .createBuyTransaction(
                                user,
                                branch,
                                new BigDecimal("1"),
                                new BigDecimal("5000"),
                                TransactionConstants.SUCCESS
                        );

        assertNotNull(transaction);

        assertEquals(
                user,
                transaction.getUser()
        );

        assertEquals(
                branch,
                transaction.getBranch()
        );

        assertEquals(
                0,
                new BigDecimal("1")
                        .compareTo(
                                transaction.getQuantity()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("5000")
                        .compareTo(
                                transaction.getAmount()
                        )
        );

        assertEquals(
                TransactionConstants.BUY,
                transaction.getTransactionType()
        );

        assertEquals(
                TransactionConstants.SUCCESS,
                transaction.getTransactionStatus()
        );

        assertNotNull(
                transaction.getCreatedAt()
        );

        verify(transactionHistoryRepository)
                .save(
                        any(TransactionHistory.class)
                );
    }

    @Test
    void createSellTransaction_ShouldCreateSuccessfully() {

        when(
                transactionHistoryRepository.save(
                        any(TransactionHistory.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );

        TransactionHistory transaction =
                transactionHistoryService
                        .createSellTransaction(
                                user,
                                branch,
                                new BigDecimal("2"),
                                new BigDecimal("10000"),
                                TransactionConstants.SUCCESS
                        );

        assertNotNull(transaction);

        assertEquals(
                user,
                transaction.getUser()
        );

        assertEquals(
                branch,
                transaction.getBranch()
        );

        assertEquals(
                0,
                new BigDecimal("2")
                        .compareTo(
                                transaction.getQuantity()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("10000")
                        .compareTo(
                                transaction.getAmount()
                        )
        );

        assertEquals(
                TransactionConstants.SELL,
                transaction.getTransactionType()
        );

        assertEquals(
                TransactionConstants.SUCCESS,
                transaction.getTransactionStatus()
        );

        assertNotNull(
                transaction.getCreatedAt()
        );

        verify(transactionHistoryRepository)
                .save(
                        any(TransactionHistory.class)
                );
    }

    @Test
    void createConvertToPhysicalTransaction_ShouldCreateSuccessfully() {

        when(
                transactionHistoryRepository.save(
                        any(TransactionHistory.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );

        TransactionHistory transaction =
                transactionHistoryService
                        .createConvertToPhysicalTransaction(
                                user,
                                branch,
                                new BigDecimal("1"),
                                new BigDecimal("5000"),
                                TransactionConstants.SUCCESS
                        );

        assertNotNull(transaction);

        assertEquals(
                user,
                transaction.getUser()
        );

        assertEquals(
                branch,
                transaction.getBranch()
        );

        assertEquals(
                0,
                new BigDecimal("1")
                        .compareTo(
                                transaction.getQuantity()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("5000")
                        .compareTo(
                                transaction.getAmount()
                        )
        );

        assertEquals(
                TransactionConstants.CONVERT_TO_PHYSICAL,
                transaction.getTransactionType()
        );

        assertEquals(
                TransactionConstants.SUCCESS,
                transaction.getTransactionStatus()
        );

        assertNotNull(
                transaction.getCreatedAt()
        );

        verify(transactionHistoryRepository)
                .save(
                        any(TransactionHistory.class)
                );
    }
}