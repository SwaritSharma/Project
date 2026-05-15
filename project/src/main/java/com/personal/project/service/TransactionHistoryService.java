package com.personal.project.service;

import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.User;
import com.personal.project.entity.VendorBranch;

import java.math.BigDecimal;

public interface TransactionHistoryService {

    TransactionHistory createBuyTransaction(
            User user,
            VendorBranch branch,
            BigDecimal quantity,
            BigDecimal amount,
            String transactionStatus
    );

    TransactionHistory createSellTransaction(
            User user,
            VendorBranch branch,
            BigDecimal quantity,
            BigDecimal amount,
            String transactionStatus
    );

    TransactionHistory
    createConvertToPhysicalTransaction(
            User user,
            VendorBranch branch,
            BigDecimal quantity,
            BigDecimal amount,
            String transactionStatus
    );
}