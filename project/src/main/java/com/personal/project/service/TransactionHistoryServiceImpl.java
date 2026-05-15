package com.personal.project.service;

import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.User;
import com.personal.project.entity.VendorBranch;
import com.personal.project.constants.TransactionConstants;
import com.personal.project.repository.TransactionHistoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionHistoryServiceImpl
        implements TransactionHistoryService {

    private final
    TransactionHistoryRepository
            transactionHistoryRepository;

    public TransactionHistoryServiceImpl(
            TransactionHistoryRepository
                    transactionHistoryRepository
    ) {

        this.transactionHistoryRepository =
                transactionHistoryRepository;
    }

    @Override
    public TransactionHistory
    createBuyTransaction(
            User user,
            VendorBranch branch,
            BigDecimal quantity,
            BigDecimal amount,
            String transactionStatus
    ) {

        TransactionHistory
                transactionHistory =
                new TransactionHistory();

        transactionHistory.setUser(user);

        transactionHistory.setBranch(branch);

        transactionHistory.setQuantity(
                quantity
        );

        transactionHistory.setAmount(
                amount
        );

        transactionHistory.setTransactionType(
                TransactionConstants.BUY
        );

        transactionHistory.setTransactionStatus(
                transactionStatus
        );

        transactionHistory.setCreatedAt(
                LocalDateTime.now()
        );

        return transactionHistoryRepository
                .save(transactionHistory);
    }

    @Override
    public TransactionHistory
    createSellTransaction(
            User user,
            VendorBranch branch,
            BigDecimal quantity,
            BigDecimal amount,
            String transactionStatus
    ) {

        TransactionHistory
                transactionHistory =
                new TransactionHistory();

        transactionHistory.setUser(user);

        transactionHistory.setBranch(branch);

        transactionHistory.setQuantity(
                quantity
        );

        transactionHistory.setAmount(
                amount
        );

        transactionHistory.setTransactionType(
                TransactionConstants.SELL
        );

        transactionHistory.setTransactionStatus(
                transactionStatus
        );

        transactionHistory.setCreatedAt(
                LocalDateTime.now()
        );

        return transactionHistoryRepository
                .save(transactionHistory);
    }

    @Override
    public TransactionHistory
    createConvertToPhysicalTransaction(
            User user,
            VendorBranch branch,
            BigDecimal quantity,
            BigDecimal amount,
            String transactionStatus
    ) {

        TransactionHistory
                transactionHistory =
                new TransactionHistory();

        transactionHistory.setUser(user);

        transactionHistory.setBranch(branch);

        transactionHistory.setQuantity(
                quantity
        );

        transactionHistory.setAmount(
                amount
        );

        transactionHistory.setTransactionType(
                TransactionConstants
                        .CONVERT_TO_PHYSICAL
        );

        transactionHistory.setTransactionStatus(
                transactionStatus
        );

        transactionHistory.setCreatedAt(
                LocalDateTime.now()
        );

        return transactionHistoryRepository
                .save(transactionHistory);
    }
}