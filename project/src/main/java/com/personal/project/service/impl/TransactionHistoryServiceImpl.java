package com.personal.project.service.impl;

import com.personal.project.service.TransactionHistoryService;

import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.User;
import com.personal.project.entity.VendorBranch;
import com.personal.project.constants.TransactionConstants;
import com.personal.project.mapper.TransactionMapper;
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

    private final
    TransactionMapper
            transactionMapper;

    public TransactionHistoryServiceImpl(
            TransactionHistoryRepository
                    transactionHistoryRepository,
            TransactionMapper
                    transactionMapper
    ) {

        this.transactionHistoryRepository =
                transactionHistoryRepository;

        this.transactionMapper =
                transactionMapper;
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

        TransactionHistory transactionHistory = transactionMapper.toEntity(
                user,
                branch,
                quantity,
                amount,
                TransactionConstants.BUY,
                transactionStatus,
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

        TransactionHistory transactionHistory = transactionMapper.toEntity(
                user,
                branch,
                quantity,
                amount,
                TransactionConstants.SELL,
                transactionStatus,
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

        TransactionHistory transactionHistory = transactionMapper.toEntity(
                user,
                branch,
                quantity,
                amount,
                TransactionConstants.CONVERT_TO_PHYSICAL,
                transactionStatus,
                LocalDateTime.now()
        );

        return transactionHistoryRepository
                .save(transactionHistory);
    }
}
